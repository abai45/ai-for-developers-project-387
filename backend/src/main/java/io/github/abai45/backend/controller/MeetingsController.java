package io.github.abai45.backend.controller;

import io.github.abai45.backend.api.MeetingsApi;
import io.github.abai45.backend.model.EventTypeView;
import io.github.abai45.backend.model.FieldError;
import io.github.abai45.backend.model.Meeting;
import io.github.abai45.backend.model.MeetingView;
import io.github.abai45.backend.service.SlotService;
import io.github.abai45.backend.store.EventTypeStore;
import io.github.abai45.backend.store.MeetingStore;
import io.github.abai45.backend.validation.EventTypeValidator;
import io.github.abai45.backend.validation.FieldErrors;
import io.github.abai45.backend.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер встреч: запись гостя по контракту MeetingsApi.
 * Окончание встречи вычисляет сервер (начало + длительность типа).
 * Правила расписания выполняет сервер: начало записи — только слот из окна
 * записи этого типа, пересечение интервалов запрещено атомарно — в том числе
 * между разными типами встреч.
 */
@RestController
public class MeetingsController implements MeetingsApi {

    private final EventTypeStore eventTypeStore;
    private final MeetingStore meetingStore;
    private final SlotService slotService;

    public MeetingsController(EventTypeStore eventTypeStore, MeetingStore meetingStore, SlotService slotService) {
        this.eventTypeStore = eventTypeStore;
        this.meetingStore = meetingStore;
        this.slotService = slotService;
    }

    @Override
    public ResponseEntity<MeetingView> createMeeting(Meeting meeting) {
        List<FieldError> errors = new ArrayList<>();
        if (meeting.getEventTypeId() == null || meeting.getEventTypeId().isBlank()) {
            errors.add(FieldErrors.of("eventTypeId", "Укажите тип встречи."));
        }
        if (meeting.getStart() == null) {
            errors.add(FieldErrors.of("start", "Выберите время встречи."));
        }
        errors.addAll(EventTypeValidator.validateGuestName(meeting.getGuestName()));
        errors.addAll(EventTypeValidator.validateTopic(meeting.getTopic()));
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        EventTypeView eventType = eventTypeStore
                .findById(meeting.getEventTypeId())
                .orElseThrow(() -> new NotFoundException("Тип встречи не найден."));

        // Сервер — источник истины расписания: запись только на слот из окна типа.
        OffsetDateTime start = meeting.getStart();
        boolean isSlot = slotService.slotsFor(eventType).stream()
                .anyMatch(slot -> slot.getStart().isEqual(start));
        if (!isSlot) {
            throw new ValidationException(
                    List.of(FieldErrors.of("start", "Слот недоступен — выберите время из календаря.")));
        }

        MeetingView view = new MeetingView();
        view.setEventTypeId(eventType.getId());
        view.setGuestName(meeting.getGuestName());
        view.setTopic(EventTypeValidator.normalizeTopic(meeting.getTopic()));
        view.setStart(start);
        view.setEnd(start.plusMinutes(eventType.getDurationMinutes()));

        if (!meetingStore.addIfNoOverlap(view)) {
            throw new ConflictException("На выбранное время только что записали встречу. Выберите другой слот.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(view);
    }

    @Override
    public ResponseEntity<List<MeetingView>> listMeetings() {
        return ResponseEntity.ok(meetingStore.upcoming(OffsetDateTime.now(SlotService.OWNER_ZONE)));
    }
}
