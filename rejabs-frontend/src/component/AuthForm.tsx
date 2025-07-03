import {useState} from "react";
import {useNavigate} from "react-router-dom";
import GithubLogin from "./GithubLogin";
import {useAuth} from "../hooks/useAuth.ts";

interface Props {
    mode: "signin" | "signup";
}

const AuthForm = ({mode}) => {
    const navigate = useNavigate();
    const isSignUp = mode === "signup";
    const {registerLocalUser, loginLocalUser} = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [username, setUsername] = useState("");
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (isSignUp && password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            if (isSignUp) {
                await registerLocalUser(username, email, password);
            } else {
                await loginLocalUser(email, password);
            }
            navigate("/home");
        } catch (err: any) {
            setError("Authentication failed. Check your credentials.");
        }
    };

    const validateEmail = (email: string) => {
        const expression = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return expression.test(email);
    };

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setEmail(e.target.value);

        // Clear errors when empty
        if (!value) {
            return;
        }

        // Validate email pattern
        if (!validateEmail(value)) {
            setError('Please enter a valid email address');
        } else {
            setError('');
        }
    };


    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 px-4">
            <div
                className="bg-white shadow-md rounded-2xl border-2 border-gray-300 p-8 max-w-md w-full text-center space-y-6">
                <p className="text-lg font-bold text-gray-600">
                    {isSignUp ? "Sign up" : "Sign in"} with your Email or GitHub account to get started.
                </p>

                <form onSubmit={handleSubmit} className="space-y-4 text-left">
                    {isSignUp ? (
                        <div>
                            <input
                                type="text"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="w-full px-3 py-2 border rounded mb-4"
                                required
                            />
                            <input
                                type="email"
                                placeholder="Email"
                                value={email}
                                onChange={handleEmailChange}
                                className={`w-full px-3 py-2 mb-4 border ${error ? 'border-red-500' : 'border-gray-300'} rounded`}
                                required
                            />
                            {error && <p className="text-red-500 text-sm mb-4 text-gray-800">{error}</p>}

                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full px-3 py-2 mb-4 border rounded" required
                            />
                            <input
                                type="password"
                                placeholder="Re-enter Password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                className="w-full px-3 py-2 mb-4 border rounded" required
                            />
                        </div>

                    ) : (
                        <div>

                            <input
                                type="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full px-3 py-2 border rounded"
                                required
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full px-3 py-2 border rounded"
                                required
                            />
                        </div>
                    )}
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-3 px-6 rounded-xl font-semibold transition-all duration-200 "
                    >
                        {isSignUp ? "Sign Up" : "Sign In"}
                    </button>
                </form>
                <div className="flex items-center my-4">
                    <div className="flex-grow border-t border-gray-300"></div>
                    <span className="mx-4 text-gray-500 text-sm">OR</span>
                    <div className="flex-grow border-t border-gray-300"></div>
                </div>
                <GithubLogin/>
            </div>
        </div>
    );
};

export default AuthForm;