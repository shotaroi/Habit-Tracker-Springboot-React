import { useState } from 'react'
import type { ComponentProps } from 'react' 
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import axios from 'axios'

type FormSubmitHandler = NonNullable<ComponentProps<'form'>['onSubmit']>

function getApiErrorMessage(err: unknown, fallback: string): string {
    if (axios.isAxiosError(err)) {
        const data = err.response?.data as { message?: string; error?: string} | undefined
        if (typeof data?.message === 'string' && data.message.trim()) return data.message
        if (typeof data?.error === 'string' && data.error.trim()) return data.error

        if (err.response?.status === 409) return 'Email already registered'
        if (err.response?.status === 400) return 'Use a valid email and password (min 8 characters)'
    }
    return fallback
}

export default function RegisterPage() {
    const navigate = useNavigate()
    const { register } = useAuth()

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    const onSubmit: FormSubmitHandler = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError(null)

        try {
            await register({ email, password})
            navigate('/', { replace: true})
        } catch (err) {
            console.error(err)
            setError('Could not create account')
        } finally {
            setLoading(false)
        }
    }

    return (
        <main className='page'>
            <h1>Register</h1>
            <form className='card' onSubmit={onSubmit}>
                <label>Email</label>
                <input 
                  type='email' 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
    
                <label>Password (min 8)</label>
                <input 
                  type='password' 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  minLength={8}
                  required
                />

                {error && <p className='error'>{error}</p>}
                <button disabled={loading} type='submit'>
                    {loading ? 'Creating...' : 'Create account'}
                </button>
            </form>

            <p>
                Already have an account? <Link to='/login'>Login</Link>
            </p>
        </main>
    )
}

