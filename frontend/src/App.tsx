import { Navigate, Route, Routes } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'
import { useAuth } from './auth/AuthContext'
import './App.css'

function AuthRedirect() {
  const { isAuthenticated } = useAuth()
  return <Navigate to={isAuthenticated ? '/' : '/login'} replace />
}

export default function App() {
  return (
    <Routes>
      <Route path='/login' element={<LoginPage />} />
      <Route path='/register' element={<RegisterPage />}></Route>

      <Route element={<ProtectedRoute />}>
        <Route path='/' element={<DashboardPage />} />
      </Route>

      <Route path='*' element={<AuthRedirect />} />
    </Routes>
  )
}