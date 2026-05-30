import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

export const api = axios.create({
    baseURL: API_BASE_URL,
})

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error?.response?.status as number | undefined

        if (status === 401) {
            localStorage.removeItem('accessToken')
            localStorage.removeItem('authUser')

            const isAuthPage = window.location.pathname === '/login' || window.location.pathname === '/register'

            if (!isAuthPage) {
                window.location.href = '/login'
            }
        }

        return Promise.reject(error)
    },
)