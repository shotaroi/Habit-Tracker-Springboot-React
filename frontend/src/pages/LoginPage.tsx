import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import type { ComponentProps } from 'react'

export default function LoginPage() {
    const navigate = useNavigate()
    const { login } = useAuth()

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    type FormSubmitHandler = NonNullable<ComponentProps<'form'>['onSubmit']>
    
    const onSubmit: FormSubmitHandler = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError(null)
        try {
            await login({ email, password})
            navigate('/', { replace: true})
        } catch {
            setError('Invalid email or password')
        } finally {
            setLoading(false)
        }
    }

    return (
        <main className='page'>
            <h1>Login</h1>
            <form className='card' onSubmit={onSubmit}>
                <label>Email</label>
                <input type='email' value={email} onChange={(e) => setEmail(e.target.value)} required />

                <label>Password</label>
                <input type='password' value={password} onChange={(e) => setPassword(e.target.value)} required />

                {error && <p className='error'>{error}</p>}
                <button disabled={loading} type='submit'>{loading ? 'Signing in...' : 'Login'}</button>
            </form>
            <p>New here? <Link to='/register'>Create account</Link></p>
        </main>
    )
}