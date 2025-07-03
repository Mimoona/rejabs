import { Navigate, Outlet } from "react-router-dom";
import {useAuth} from "../hooks/useAuth.ts";


export default function ProtectedRoute() {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return <h3>Loading...</h3>; // still checking token or OAuth state
    }

    if (!user) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}