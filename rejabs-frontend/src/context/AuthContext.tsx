import {createContext, useEffect, useMemo, useState} from "react";
import api from "../api/axios.ts";
import type {AuthContextType, User} from "../types/User.ts";



export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children}: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | undefined | null>(undefined);
    const [isLoading, setIsLoading] = useState(true);

    const loadUser = async () => {
        try {
            const response = await api.get("/auth");
            if (response.data) {
                setUser(response.data)
            } else {
                setUser(null);
            }
        } catch (error) {
            console.error("Error fetching data:", error);
            setUser(null);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(()=>{
        loadUser();
    }, []);

    const login = () => {
        const isLocal = window.location.host === "localhost:5173";
        const host = isLocal ? "http://localhost:8080" : window.location.origin;

        // Check if JSESSIONID cookie exists
        const hasSession = document.cookie
            .split("; ")
            .some(cookie => cookie.startsWith("JSESSIONID="));

        if (hasSession && window.location.pathname === "/signup") {
            // User is likely authenticated
            window.location.href = "/home";
        } else {
            // Redirect to GitHub OAuth
            window.open(`${host}/oauth2/authorization/github`, "_self");
        }
    };

    const logout = async () => {
        try {
            const response = await api.post("/logout");
            console.log("Logout successful:", response.data);
            setUser(null);
        } catch (error) {
            console.error("Logout failed:", error);
        }
    };

    // Wrap the context value in useMemo
    const value = useMemo(
        () => ({
            user,
            setUser,
            login,
            logout,
            isLoading,
        }),
        [user, isLoading] // Dependencies array includes all values that should trigger a recalculation
    );

    return (
        <AuthContext.Provider value={value}>
            {isLoading ? <div>Loading...</div> :children}
        </AuthContext.Provider>
    );
};

