import { useCallback, useEffect, useState } from 'react'
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardHeader,
} from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { listEventTypes, listMeetings, type MeetingView } from '@/lib/api/client'
import { formatOwnerDateTimeRange } from '@/lib/ownerTime'

type SectionState =
  | { status: 'loading' }
  | { status: 'error' }
  | { status: 'ok'; meetings: MeetingView[]; typeNames: Map<string, string> }

export default function UpcomingMeetingsSection() {
  const [state, setState] = useState<SectionState>({ status: 'loading' })
  const [reloadKey, setReloadKey] = useState(0)

  useEffect(() => {
    let cancelled = false
    Promise.all([listMeetings(), listEventTypes().catch(() => [])])
      .then(([meetings, types]) => {
        if (!cancelled) {
          setState({
            status: 'ok',
            meetings,
            typeNames: new Map(types.map(t => [t.id, t.name])),
          })
        }
      })
      .catch(() => {
        if (!cancelled) setState({ status: 'error' })
      })
    return () => {
      cancelled = true
    }
  }, [reloadKey])

  const reload = useCallback(() => {
    setState({ status: 'loading' })
    setReloadKey(key => key + 1)
  }, [])

  return (
    <Card>
      <CardHeader>
        <h2 className="font-heading text-base leading-snug font-medium">
          Предстоящие встречи
        </h2>
      </CardHeader>
      <CardContent>
        {state.status === 'loading' && (
          <div className="flex flex-col gap-3">
            <p className="sr-only" aria-live="polite">
              Загрузка…
            </p>
            <Skeleton className="h-16 w-full rounded-xl" />
            <Skeleton className="h-16 w-full rounded-xl" />
          </div>
        )}
        {state.status === 'error' && (
          <div className="flex flex-col items-start gap-3" role="alert">
            <p className="text-muted-foreground">Не удалось загрузить данные</p>
            <Button variant="outline" onClick={reload}>
              Повторить
            </Button>
          </div>
        )}
        {state.status === 'ok' && state.meetings.length === 0 && (
          <div className="flex flex-col gap-1">
            <p className="font-medium">Предстоящих встреч пока нет</p>
            <p className="text-muted-foreground">
              Здесь появятся встречи, на которые запишутся гости.
            </p>
          </div>
        )}
        {state.status === 'ok' && state.meetings.length > 0 && (
          <ul className="flex flex-col gap-3">
            {state.meetings.map(meeting => (
              <li key={`${meeting.eventTypeId}-${meeting.start}`}>
                <MeetingCard
                  meeting={meeting}
                  typeName={state.typeNames.get(meeting.eventTypeId) ?? 'Тип встречи'}
                />
              </li>
            ))}
          </ul>
        )}
      </CardContent>
    </Card>
  )
}

function MeetingCard({ meeting, typeName }: { meeting: MeetingView; typeName: string }) {
  return (
    <div className="flex flex-col gap-1 rounded-xl border p-4">
      <p className="font-medium">{typeName}</p>
      <p className="text-muted-foreground text-sm">Гость: {meeting.guestName}</p>
      {/* Строка темы — только при непустом значении; без темы вид карточки не меняется. */}
      {meeting.topic ? <p className="text-muted-foreground text-sm">Тема: {meeting.topic}</p> : null}
      <p className="text-muted-foreground text-sm">
        {formatOwnerDateTimeRange(meeting.start, meeting.end)}
      </p>
    </div>
  )
}
