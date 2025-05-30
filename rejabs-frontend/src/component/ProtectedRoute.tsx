import { Navigate, Outlet } from "react-router-dom";
import {useAuth} from "../features/auth/AuthContext.tsx";


export default function ProtectedRoute() {
    const { user } = useAuth();

    if (user === undefined) {
        return <h3>Loading...</h3>; // waiting for auth check
    }

    return user ? <Outlet /> : <Navigate to="/" replace />;
}