import BookingInfoCard from '@/components/booking/BookingInfoCard'
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardHeader,
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import type { EventTypeView, Slot } from '@/lib/api/client'

export default function NameStep({
  eventType,
  selectedDay,
  selectedSlot,
  guestName,
  topic,
  onNameChange,
  onTopicChange,
  onBack,
  onNext,
}: {
  eventType: EventTypeView
  selectedDay: string
  selectedSlot: Slot
  guestName: string
  topic: string
  onNameChange: (name: string) => void
  onTopicChange: (topic: string) => void
  onBack: () => void
  onNext: () => void
}) {
  return (
    <div className="flex flex-col gap-4 md:grid md:grid-cols-2 md:items-start">
      <BookingInfoCard eventType={eventType} day={selectedDay} slot={selectedSlot} />
      <Card>
        <CardHeader>
          <h2 className="font-heading text-base leading-snug font-medium">Ваше имя</h2>
        </CardHeader>
        <CardContent>
          <form
            id="guest-name-form"
            onSubmit={event => {
              event.preventDefault()
              onNext()
            }}
            className="flex flex-col gap-4"
          >
            <div className="flex flex-col gap-1.5">
              <Label htmlFor="guest-name">Ваше имя</Label>
              <Input
                id="guest-name"
                value={guestName}
                onChange={event => onNameChange(event.target.value)}
                required
                maxLength={100}
              />
            </div>
            <div className="flex flex-col gap-1.5">
              <Label htmlFor="guest-topic">Тема разговора</Label>
              <Input
                id="guest-topic"
                value={topic}
                onChange={event => onTopicChange(event.target.value)}
                maxLength={100}
                aria-describedby="guest-topic-hint"
              />
              <p id="guest-topic-hint" className="text-muted-foreground text-xs">
                Необязательно, до 100 символов
              </p>
            </div>
          </form>
          <div className="mt-4 flex justify-between">
            <Button variant="outline" onClick={onBack}>
              Назад
            </Button>
            {/* Продолжить запускает нативную клиентскую проверку формы (пустое имя не пропускается). */}
            <Button type="submit" form="guest-name-form">
              Продолжить
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
