import { createContext, useContext, useMemo, useState } from 'react'
import { api } from '../lib/api'
import type { AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './types'

type AuthContextValue = {
    user: AuthUser | null
    accessToken: string | null
    isAuthenticated: boolean
    login: (payload: LoginRequest) => Promise<void>
    register: (payload: RegisterRequest) => Promise<void>
    logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined) 

const ACCESS_TOKEN_KEY = 'accessToken'
const AUTH_USER_KEY = 'authUser'

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [accessToken, setAccessToken] = useState<string | null>(
        localStorage.getItem(ACCESS_TOKEN_KEY),
    )
    const [user, setUser] = useState<AuthUser | null>(() => {
        const raw = localStorage.getItem(AUTH_USER_KEY)
        return raw ? (JSON.parse(raw) as AuthUser) : null
    })

    const applyAuth = (data: AuthResponse) => {
        setAccessToken(data.accessToken)
        setUser(data.user)
        localStorage.setItem(ACCESS_TOKEN_KEY, data.accessToken)
        localStorage.setItem(AUTH_USER_KEY, JSON.stringify(data.user))
    }

    const login = async (payload: LoginRequest) => {
        const { data } = await api.post<AuthResponse>('/api/auth/login', payload)
        applyAuth(data)
    }

    const register = async (payload: RegisterRequest) => {
        const { data } = await api.post<AuthResponse>('/api/auth/register', payload)
        applyAuth(data)
    }

    const logout = () => {
        setAccessToken(null)
        setUser(null)
        localStorage.removeItem(ACCESS_TOKEN_KEY)
        localStorage.removeItem(AUTH_USER_KEY)
    }

    const value = useMemo<AuthContextValue>(
        () => ({
            user,
            accessToken,
            isAuthenticated: Boolean(accessToken),
            login,
            register,
            logout,
        }),
        [user, accessToken],
    )

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>   
}

export function useAuth() {
    const ctx = useContext(AuthContext)
    if (!ctx) throw new Error('useAuth must be used within AuthProvider')
    return ctx
}