import {useAuth} from "../hooks/useAuth.ts";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const {loginOauthUser, registerLocalUser, loginLocalUser} = useAuth();

    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [emailError, setEmailError] = useState('');
    const [formError, setFormError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const validateEmail = (email: string) => {
        const expression = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return expression.test(email);
    };


    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setEmail(value);

        // Clear errors when empty
        if (!value) {
            setEmailError('');
            setFormError('');
            return;
        }

        // Validate email pattern in real-time
        if (!validateEmail(value)) {
            setEmailError('Please enter a valid email address');
        } else {
            setEmailError('');
        }
        setFormError('');
    };


    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setFormError('');
        setIsSubmitting(true);

        if (!validateEmail(email)) {
            setEmailError('Please enter a valid email address');
            setIsSubmitting(false);
            return;
        }

        try {
            // 1. Register the user
            const registrationData = await registerLocalUser(username, email, password);
            console.log(registrationData);

            // 2. Check if we got a token directly from registration
            if (registrationData) {
                // Registration included automatic login (preferred)
                navigate('/home');
                return;
            }

            // 3. If no token, attempt explicit login
            try {
                await loginLocalUser(email, password);
                navigate('/home');
            } catch (loginError) {
                console.error('Auto-login failed:', loginError);
                setFormError('Registration successful! Please log in manually.');
            }
        } catch (error: any) {
            console.error('Registration error:', error);

            if (error.response) {
                if (error.response.status === 409) {
                    setEmailError('Email already exists');
                } else {
                    setFormError(error.response.data?.message || 'Registration failed');
                }
            } else {
                setFormError(error.message || 'An error occurred');
            }
        } finally {
            setIsSubmitting(false);
        }

    };

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setFormError('');

        if (!validateEmail(email)) {
            setEmailError('Please enter a valid email address');
            return;
        }
        setEmailError('');

        try {
            await loginLocalUser(email, password);
        } catch (error) {
            console.error('Login failed:', error);
            setFormError('Login failed. Please check your credentials.');
        }

    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 px-4">
            <div
                className="bg-white shadow-md rounded-2xl border-2 border-gray-300 p-8 max-w-md w-full text-center space-y-6">
                <h1 className="text-2xl font-bold text-gray-800">Welcome to ReJaBs</h1>
                <p className="text-gray-600">Sign up or log in with your Email or GitHub account to get started.</p>

                {formError && (
                    <div className="text-red-500 text-sm">{formError}</div>
                )}

                <form className="space-y-4 text-left">
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={handleEmailChange}
                            onBlur={() => {
                                if (email && !validateEmail(email)) {
                                    setEmailError('Please enter a valid email address');
                                }
                            }}
                            className={`w-full px-4 py-2 border ${emailError ? 'border-red-500' : 'border-gray-300'} rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500`}
                            placeholder="your@email.com"
                            required
                            pattern="[^\s@]+@[^\s@]+\.[^\s@]+"
                            title="Please enter a valid email address (e.g., test@mail.com)"
                        />
                        {emailError && (
                            <p className="mt-1 text-sm text-red-500">{emailError}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-1">
                            Username
                        </label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                            placeholder="username"
                            minLength={3}
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                            required
                        />
                    </div>

                    <div className="flex gap-4">
                        <button
                            type="submit"
                            onClick={handleRegister}
                            className={`w-full bg-indigo-600 hover:bg-indigo-700 text-white py-3 px-6 rounded-xl font-semibold transition-all duration-200 ${
                                isSubmitting ? 'opacity-50 cursor-not-allowed' : ''
                            }`}
                        >
                            {isSubmitting ? 'Processing...' : 'Sign Up'}
                        </button>
                        <button
                            type="submit"
                            onClick={handleLogin}
                            disabled={isSubmitting}
                            className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-3 px-6 rounded-xl font-semibold transition-all duration-200 "
                        >
                            Log in
                        </button>
                    </div>
                </form>

                {/* Divider */}
                <div className="flex items-center my-4">
                    <div className="flex-grow border-t border-gray-300"></div>
                    <span className="mx-4 text-gray-500 text-sm">Or continue with</span>
                    <div className="flex-grow border-t border-gray-300"></div>
                </div>

                <div className="border-t border-gray-200 pt-4">
                    <button
                        onClick={loginOauthUser}
                        className="w-full bg-gray-800 hover:bg-gray-900 text-white py-3 px-6 rounded-xl font-semibold transition-all duration-200"
                    >
                        Continue with GitHub
                    </button>
                </div>
            </div>
        </div>
    );
};
export default Login;