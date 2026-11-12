import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { toast } from 'sonner'
import BookingStepper from '@/components/booking/BookingStepper'
import CalendarStep from '@/components/booking/CalendarStep'
import ConfirmStep from '@/components/booking/ConfirmStep'
import EventTypeSelectStep from '@/components/booking/EventTypeSelectStep'
import NameStep from '@/components/booking/NameStep'
import { buttonVariants } from '@/components/ui/button'
import { createMeeting, type EventTypeView, type Slot } from '@/lib/api/client'

export default function BookingPage() {
  const navigate = useNavigate()
  const [step, setStep] = useState(1)
  const [eventType, setEventType] = useState<EventTypeView | null>(null)
  const [selectedDay, setSelectedDay] = useState<string | null>(null)
  const [selectedSlot, setSelectedSlot] = useState<Slot | null>(null)
  const [guestName, setGuestName] = useState('')
  const [topic, setTopic] = useState('')
  const [pending, setPending] = useState(false)
  const [conflictMessage, setConflictMessage] = useState<string | null>(null)

  function pickType(next: EventTypeView) {
    // Смена типа сбрасывает выбранный слот и день.
    setEventType(next)
    setSelectedDay(null)
    setSelectedSlot(null)
    setStep(2)
  }

  function goToStep(target: number) {
    // Возврат возможен только на пройденные шаги.
    if (target < step && target >= 1) setStep(target)
  }

  async function confirmBooking() {
    if (!eventType || !selectedSlot || pending) return
    setPending(true)
    try {
      const trimmedTopic = topic.trim()
      const result = await createMeeting({
        eventTypeId: eventType.id,
        guestName,
        start: selectedSlot.start,
        // Тема опциональна: в тело запроса попадает только при непустом значении.
        ...(trimmedTopic ? { topic } : {}),
      })
      if (result.kind === 'created') {
        navigate('/book/success', {
          state: { meeting: result.meeting, eventTypeName: eventType.name },
        })
        return
      }
      if (result.kind === 'conflict') {
        // 409: возврат на «Календарь» с перезапросом слотов (шаг remount-ится),
        // выбранный слот сброшен, тип и имя сохранены.
        setConflictMessage('Слот только что заняли, выберите другой')
        setSelectedSlot(null)
        setStep(2)
        return
      }
      toast.error(result.detail)
    } catch {
      toast.error('Не удалось записаться. Попробуйте ещё раз.')
    } finally {
      setPending(false)
    }
  }

  return (
    <section className="mx-auto flex w-full max-w-5xl flex-col gap-6 px-4 py-10">
      <div className="flex items-center justify-between gap-4">
        <h1 className="font-heading text-3xl font-semibold">Присоединиться ко встрече</h1>
        <Link to="/" className={buttonVariants({ variant: 'outline' })}>
          На главную
        </Link>
      </div>

      <BookingStepper current={step} onStepClick={goToStep} />

      {step === 1 && <EventTypeSelectStep onPick={pickType} />}

      {step === 2 && eventType && (
        <CalendarStep
          eventType={eventType}
          selectedDay={selectedDay}
          selectedSlot={selectedSlot}
          conflictMessage={conflictMessage}
          onPickDay={day => {
            setSelectedDay(day)
            setSelectedSlot(null)
          }}
          onPickSlot={slot => {
            setSelectedSlot(slot)
            setConflictMessage(null)
          }}
          onBack={() => {
            setConflictMessage(null)
            setStep(1)
          }}
          onNext={() => setStep(3)}
        />
      )}

      {step === 3 && eventType && selectedDay && selectedSlot && (
        <NameStep
          eventType={eventType}
          selectedDay={selectedDay}
          selectedSlot={selectedSlot}
          guestName={guestName}
          topic={topic}
          onNameChange={setGuestName}
          onTopicChange={setTopic}
          onBack={() => setStep(2)}
          onNext={() => setStep(4)}
        />
      )}

      {step === 4 && eventType && selectedDay && selectedSlot && (
        <ConfirmStep
          eventType={eventType}
          selectedDay={selectedDay}
          selectedSlot={selectedSlot}
          guestName={guestName}
          pending={pending}
          onBack={() => setStep(3)}
          onConfirm={confirmBooking}
        />
      )}
    </section>
  )
}
