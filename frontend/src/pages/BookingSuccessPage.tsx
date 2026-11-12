import { Link, Navigate, useLocation } from 'react-router-dom'
import { buttonVariants } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardHeader,
} from '@/components/ui/card'
import { formatOwnerDateTimeRange } from '@/lib/ownerTime'
import type { MeetingView } from '@/lib/api/client'

interface SuccessState {
  meeting: MeetingView
  eventTypeName: string
}

export default function BookingSuccessPage() {
  const location = useLocation()
  const state = location.state as SuccessState | null

  // Прямой заход или F5 без состояния мастера — редирект на /book.
  if (!state?.meeting || !state.eventTypeName) {
    return <Navigate to="/book" replace />
  }

  const { meeting, eventTypeName } = state

  return (
    <section className="mx-auto flex w-full max-w-2xl flex-col items-center gap-6 px-4 py-16">
      <h1 className="font-heading text-3xl font-semibold">Вы записаны</h1>
      <Card className="w-full">
        <CardHeader>
          <h2 className="font-heading text-base leading-snug font-medium">
            {eventTypeName}
          </h2>
        </CardHeader>
        <CardContent className="flex flex-col gap-1 text-sm">
          <p>Гость: {meeting.guestName}</p>
          {meeting.topic ? <p>Тема: {meeting.topic}</p> : null}
          <p className="text-muted-foreground">
            {formatOwnerDateTimeRange(meeting.start, meeting.end)}
          </p>
        </CardContent>
      </Card>
      <Link to="/" className={buttonVariants({ variant: 'outline' })}>
        На главную
      </Link>
    </section>
  )
}
