package io.github.abai45.backend.validation;

import io.github.abai45.backend.model.EventType;
import io.github.abai45.backend.model.FieldError;
import java.util.ArrayList;
import java.util.List;

/**
 * Серверная валидация типа встречи — сервер источник истины (см. docs/decisions.md).
 * Собирает все ошибки полей за один проход; сообщения на русском показываются
 * прямо у поля в форме владельца.
 */
public final class EventTypeValidator {

    private EventTypeValidator() {}

    public static List<FieldError> validate(EventType eventType) {
        List<FieldError> errors = new ArrayList<>();

        String name = eventType.getName();
        if (name == null || name.isBlank()) {
            errors.add(FieldErrors.of("name", "Название обязательно (1–100 символов)."));
        } else if (name.length() > 100) {
            errors.add(FieldErrors.of("name", "Название не длиннее 100 символов."));
        }

        String description = eventType.getDescription();
        if (description != null && description.length() > 500) {
            errors.add(FieldErrors.of("description", "Описание не длиннее 500 символов."));
        }

        Integer duration = eventType.getDurationMinutes();
        if (duration == null) {
            errors.add(FieldErrors.of("durationMinutes", "Длительность обязательна (1–510 минут)."));
        } else if (duration < 1 || duration > 510) {
            errors.add(FieldErrors.of("durationMinutes", "Длительность должна быть от 1 до 510 минут."));
        }

        Integer step = eventType.getSlotStepMinutes();
        if (step == null || step < 1) {
            errors.add(FieldErrors.of("slotStepMinutes", "Шаг слота должен быть не меньше 1 минуты."));
        } else if (duration != null && step > duration) {
            // Сравнение с длительностью — даже когда сама длительность невалидна:
            // шаг не может превышать заявленную длительность ни при каких значениях.
            errors.add(FieldErrors.of("slotStepMinutes", "Шаг слота не может превышать длительность."));
        }

        return errors;
    }

    /** Имя гостя обязательно и не длиннее 100 символов (встреча, тикет #11). */
    public static List<FieldError> validateGuestName(String guestName) {
        List<FieldError> errors = new ArrayList<>();
        if (guestName == null || guestName.isBlank()) {
            errors.add(FieldErrors.of("guestName", "Имя обязательно (1–100 символов)."));
        } else if (guestName.length() > 100) {
            errors.add(FieldErrors.of("guestName", "Имя не длиннее 100 символов."));
        }
        return errors;
    }

    /**
     * Тема разговора опциональна: отсутствие и пустая строка — не ошибка;
     * при присутствии длина не больше 100 символов (встреча, тикет #14).
     */
    public static List<FieldError> validateTopic(String topic) {
        List<FieldError> errors = new ArrayList<>();
        if (topic != null && topic.length() > 100) {
            errors.add(FieldErrors.of("topic", "Тема не длиннее 100 символов."));
        }
        return errors;
    }

    /** Пустая или пробельная тема нормализуется к отсутствию (null). */
    public static String normalizeTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            return null;
        }
        return topic;
    }
}
