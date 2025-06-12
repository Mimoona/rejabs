import {useAuth} from "../hooks/useAuth.ts";

const Login = () => {
    const {login} = useAuth();

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 px-4">
            <div className="bg-white shadow-md rounded-2xl p-8 max-w-md w-full text-center space-y-6">
                <h1 className="text-2xl font-bold text-gray-800">Welcome to ReJaBs</h1>
                <p className="text-gray-600">Sign up or log in with your GitHub account to get started.</p>

                <button
                    onClick={login}
                    className="w-full bg-gray-800 hover:bg-gray-900 text-white py-3 px-6 rounded-xl font-semibold transition-all duration-200"
                >
                    Continue with GitHub
                </button>

                <div className="border-t border-gray-200 pt-4">
                    <p className="text-sm text-gray-500">Already have an account?</p>
                    <button
                        onClick={login}
                        className="mt-2 text-indigo-600 hover:underline font-medium"
                    >
                        Log in with GitHub
                    </button>
                </div>
            </div>
        </div>

    );
};
export default Login;