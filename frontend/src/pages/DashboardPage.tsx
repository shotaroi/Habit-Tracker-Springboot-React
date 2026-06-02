import { useEffect, useState } from 'react'
import type { ComponentProps } from 'react'
import { useAuth } from '../auth/AuthContext'
import { api } from '../lib/api'

type Habit = {
    id: number
    name: string
    frequency: string
    targetDays: number
    archived: boolean
    createdAt: string
}

type DayProgress = {
    date: string
    completedHabits: number
    totalHabits: number
}

type WeeklyProgressResponse = {
    days: DayProgress[]
    currentStreak: number
    bestStreak: number
}

type FormSubmitHandler = NonNullable<ComponentProps<'form'>['onSubmit']>

export default function DashboardPage() {
    const { user, logout } = useAuth()

    const [habits, setHabits] = useState<Habit[]>([])
    const [completedToday, setCompletedToday] = useState<Set<number>>(new Set())
    const [progress, setProgress] = useState<WeeklyProgressResponse | null>(null)
    const [habitName, setHabitName] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const loadData = async () => {
        setLoading(true)
        setError(null)
        try {
            const [habitsRes, progressRes, completedRes] = await Promise.all([
                api.get<Habit[]>('/api/habits'),
                api.get<WeeklyProgressResponse>('/api/progress/weekly'),
                api.get<number[]>('/api/habits/completed'),
            ])
            setHabits(habitsRes.data)
            setProgress(progressRes.data)
            setCompletedToday(new Set(completedRes.data))
        } catch {
            setError('Failed to load dashboard data')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        void loadData()
    }, [])

    const onCreateHabit: FormSubmitHandler = async (e) => {
        e.preventDefault()
        const trimmed = habitName.trim()
        if (!trimmed) return

        try {
            await api.post('/api/habits', {
                name: trimmed,
                frequency: 'DAILY',
                targetDays: 7,
            })
            setHabitName('')
            await loadData()
        } catch {
            setError('Failed to create habit')
        }
    }

    const onCompleteHabit = async (habitId: number) => {
        try {
            await api.post(`/api/habits/${habitId}/complete`)
            setCompletedToday((prev) => new Set(prev).add(habitId))
            await loadData()
        } catch {
            setError('Failed to complete habit')
        }
    }

    const onArchivedHabit = async (habitId: number) => {
        try {
            await api.post(`/api/habits/${habitId}/archive`)
            await loadData()
        } catch {
            setError('Failed to archive habit')
        }
    }

    return (
        <main className='page'>
            <header className='row' style={{ marginBottom: '1rem'}}>
                <h1>Habit Dashboard</h1>
                <div className='row'>
                    <span>{user?.email}</span>
                    <button onClick={logout}>Logout</button>
                </div>
            </header>

            <section className='card'>
                <h2>Create Habit</h2>
                <form className='row' onSubmit={onCreateHabit}>
                    <input 
                      type='text' 
                      placeholder='Habit name'
                      value={habitName}
                      onChange={(e) => setHabitName(e.target.value)}
                      required
                    />
                    <button type='submit'>Add</button>
                </form>
            </section>

            <section className='card'>
                <h2>Your Habits</h2>
                {loading ? (
                    <p>Loading...</p>
                ) : habits.length === 0 ? (
                    <p>No habits yet</p>
                ) : (
                    <ul>
                      {habits.map((habit) => {
                        const done = completedToday.has(habit.id)
                        return (
                            <li key={habit.id} className='row' style={{ marginBottom: '0.5rem'}}>
                                <span>{habit.name}</span>
                                <button disabled={done} onClick={() => onCompleteHabit(habit.id)}>
                                    {done ? 'Completed today' : 'Complete today'} 
                                </button>    
                                <button onClick={() => onArchivedHabit(habit.id)}>Archive</button>
                            </li>
                        )
                    })}
                    </ul>
                )}
            </section>

            <section className='card'>
                <h2>Weekly Progress</h2>
                {progress ? (
                    <>
                      <p>Current streak: {progress.currentStreak}</p>
                      <p>Best streak: {progress.bestStreak}</p>
                      <ul>
                        {progress.days.map((day) => (
                            <li key={day.date}>
                                {day.date}: {day.completedHabits} / {day.totalHabits}
                            </li>
                        ))}
                      </ul>
                    </>
                ) : (
                    <p>No progress data yet.</p>
                )}
            </section>

            {error && (
                <section className='card'>
                    <p className='error'>{error}</p>
                </section>
            )}
        </main>
    )
}