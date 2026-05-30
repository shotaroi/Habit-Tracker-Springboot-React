export type AuthUser = {
    id: number
    email: string
}

export type AuthResponse = {
    accessToken: string
    tokenType: string
    expiresIn: number
    user: AuthUser
}

export type LoginRequest = {
    email: string
    password: string
}

export type RegisterRequest = {
    email: string
    password: string
}