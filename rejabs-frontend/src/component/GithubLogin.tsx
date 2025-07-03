import {useAuth} from "../hooks/useAuth.ts";


const GithubLogin = () => {
    const { loginOauthUser } = useAuth();

    return (
        <button
            onClick={loginOauthUser}
            className="bg-black text-white py-2 px-4 rounded hover:bg-gray-800 w-full"
        >
            Continue with GitHub
        </button>
    );
};

export default GithubLogin;