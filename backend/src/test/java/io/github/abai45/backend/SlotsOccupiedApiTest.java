package io.github.abai45.backend;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.abai45.backend.model.EventType;
import io.github.abai45.backend.model.EventTypeView;
import io.github.abai45.backend.model.Meeting;
import io.github.abai45.backend.model.Slot;
import io.github.abai45.backend.model.SlotStatus;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

/**
 * API-тест занятости слотов (шов HTTP): слот, пересекающийся с существующей
 * встречей (в том числе другого типа), возвращается occupied; касание
 * интервалов остаётся free; до записи слот свободен, после — занят.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
// Встречи этого класса не должны протекать в контекст других тестовых классов
// (MeetingsApiTest бронирует тот же слот «завтра 10:00» и ждёт 201, а
// SlotsApiTest проверяет, что без записей все слоты свободны).
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SlotsOccupiedApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void bookedSlotBecomesOccupiedAndTouchingSlotsStayFree() {
        EventTypeView type = createType("Занятость 30 на 15", 30, 15);

        Slot target = slotAt(type.getId(), 0, 2); // старт 10:00 (день 0, шаг 15)
        assertThat(statusOf(type.getId(), target.getStart())).isEqualTo(SlotStatus.FREE);

        assertThat(book(type.getId(), "Гость", target.getStart()).getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        // Забронированный слот и слот, частично его накрывающий (09:45–10:15), — occupied.
        assertThat(statusOf(type.getId(), target.getStart())).isEqualTo(SlotStatus.OCCUPIED);
        Slot overlapping = slotAt(type.getId(), 0, 1); // 09:45–10:15
        assertThat(statusOf(type.getId(), overlapping.getStart())).isEqualTo(SlotStatus.OCCUPIED);
        // Предыдущий слот 09:30–10:00 только касается встречи — свободен.
        Slot touching = slotAt(type.getId(), 0, 0);
        assertThat(statusOf(type.getId(), touching.getStart())).isEqualTo(SlotStatus.FREE);
    }

    @Test
    void meetingOfAnotherEventTypeOccupiesOverlappingSlots() {
        EventTypeView shortType = createType("Короткий 30 на 15", 30, 15);
        EventTypeView longType = createType("Длинный 60 на 30", 60, 30);

        // Встреча другого типа: 09:30–10:30 в первом дне окна.
        Slot longSlot = slotAt(longType.getId(), 1, 0);
        assertThat(book(longType.getId(), "Длинный гость", longSlot.getStart()).getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        // Слоты короткого типа 09:30 и 09:45 пересекаются с 09:30–10:30 — заняты.
        assertThat(statusOf(shortType.getId(), slotAt(shortType.getId(), 1, 0).getStart()))
                .isEqualTo(SlotStatus.OCCUPIED);
        assertThat(statusOf(shortType.getId(), slotAt(shortType.getId(), 1, 1).getStart()))
                .isEqualTo(SlotStatus.OCCUPIED);
        // Слот 10:30–11:00 только касается окончания длинной встречи — свободен.
        assertThat(statusOf(shortType.getId(), slotAt(shortType.getId(), 1, 4).getStart()))
                .isEqualTo(SlotStatus.FREE);
    }

    private EventTypeView createType(String name, int duration, int step) {
        EventType request = new EventType();
        request.setName(name);
        request.setDurationMinutes(duration);
        request.setSlotStepMinutes(step);
        ResponseEntity<EventTypeView> response =
                restTemplate.exchange("/api/event-types", HttpMethod.POST, json(request), EventTypeView.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }

    private List<Slot> listSlots(String eventTypeId) {
        ResponseEntity<Slot[]> response =
                restTemplate.exchange("/api/event-types/{id}/slots", HttpMethod.GET, null, Slot[].class, eventTypeId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return List.of(response.getBody());
    }

    private Slot slotAt(String eventTypeId, int dayIndex, int slotIndex) {
        List<Slot> slots = listSlots(eventTypeId);
        long perDay = slots.stream()
                .filter(slot -> slotToDay(slot) == slotToDay(slots.get(0)))
                .count();
        return slots.get(dayIndex * (int) perDay + slotIndex);
    }

    private static long slotToDay(Slot slot) {
        return slot.getStart()
                .atZoneSameInstant(java.time.ZoneId.of("Asia/Almaty"))
                .toLocalDate()
                .toEpochDay();
    }

    private SlotStatus statusOf(String eventTypeId, OffsetDateTime start) {
        return listSlots(eventTypeId).stream()
                .filter(slot -> slot.getStart().isEqual(start))
                .map(Slot::getStatus)
                .findFirst()
                .orElseThrow(() -> new AssertionError("слот не найден: " + start));
    }

    private ResponseEntity<String> book(String eventTypeId, String guestName, OffsetDateTime start) {
        Meeting meeting = new Meeting();
        meeting.setEventTypeId(eventTypeId);
        meeting.setGuestName(guestName);
        meeting.setStart(start);
        return restTemplate.exchange("/api/meetings", HttpMethod.POST, json(meeting), String.class);
    }

    private static HttpEntity<Object> json(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
