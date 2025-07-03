import {createContext, useEffect, useMemo, useState} from "react";
import api from "../api/axios.ts";
import type {AuthContextType, User} from "../types/User.ts";
import {useNavigate} from "react-router-dom";
import axios from "axios";

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children}: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | undefined | null>(undefined);
    const [isLoading, setIsLoading] = useState(true);
    // const navigate = useNavigate();

    const loadUser = async () => {

        const params = new URLSearchParams(window.location.search);
        const tokenFromUrl = params.get("token");

        if (tokenFromUrl) {
            localStorage.setItem("token", tokenFromUrl);
        }

        const token = tokenFromUrl || localStorage.getItem("token");
        if (!token) {
            setUser(null);
            setIsLoading(false);
            return;
        }

        try {
            const response = await api.get("/auth/user")
            console.log(response);
            setUser(response.data.user);
        } catch (error) {
            console.error("Error fetching data:", error?.response || error);
            setUser(null);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadUser();

    }, []);

    const loginOauthUser = () => {
        const isLocal = window.location.host === "localhost:5173";
        const host = isLocal ? "http://localhost:8080" : window.location.origin;

        const authPages = ["/signup", "/signin"];
        const token = localStorage.getItem("token");
        if (token && authPages.includes(window.location.pathname)) {
            // User is likely authenticated
            window.location.href = "/home";
        } else {
            // Redirect to GitHub OAuth
            window.open(`${host}/oauth2/authorization/github`, "_self");
        }
    };

    const registerLocalUser = async (username: string, email: string, password: string) => {
        try {
            const response = await api.post("/auth/register", {
                username,
                email,
                password,
            });

            if (response.data?.token) {
                localStorage.setItem("token", response.data.token);
                setUser(response.data.user);
                return response.data;
            }
            throw new Error("Registration succeeded but no token received");
        } catch (error) {
            console.error("Registration failed:", error);
            throw error;
        }
    };
    const loginLocalUser = async (email: string, password: string) => {
        try {
            const response = await api.post("/auth/login", {
                email,
                password,
            });

            if (response.data?.token) {
                localStorage.setItem("token", response.data.token);
                setUser(response.data.user);
                return response.data;
            }
            throw new Error("Login succeeded but no token received");
        } catch (error) {
            console.error("Login failed:", error);
            throw error;
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
            loginOauthUser,
            registerLocalUser,
            loginLocalUser,
            logout,
            isLoading,
        }),
        [user, isLoading] // Dependencies array includes all values that should trigger a recalculation
    );

    return (
        <AuthContext.Provider value={value}>
            {isLoading ? <div>Loading...</div> : children}
        </AuthContext.Provider>
    );
};

