import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import api from "../api/axios.ts";

const OAuthCallback = () => {
    const navigate = useNavigate();
    const { setUser } = useAuth();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get("token");

        if (token) {
            localStorage.setItem("token", token);
            console.log(token);

            // Now fetch the user using the token
            fetchUser();
        } else {
            navigate("/"); // Fallback
        }
    }, []);

    const fetchUser = async () => {
        try {
            const response = await api.get("/auth/user");
            setUser(response.data);
            navigate("/home");
        } catch (error) {
            console.error("Failed to fetch user:", error);
            navigate("/");
        }
    };

    return <div>Signing you in...</div>;
};

export default OAuthCallback;