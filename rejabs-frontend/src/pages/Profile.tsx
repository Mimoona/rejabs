import {useAuth} from "../hooks/useAuth.ts";

const Profile = () => {
    const {user} = useAuth();

    const name = user?.login ?? "Not Available";
    const email = user?.email ?? "Not Available";
    const avatarUrl = user?.avatar_url ?? "/default-avatar.png";


    return (
        <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-2xl shadow-md">
            <div className="flex flex-col items-center mb-6">
                <img
                    src={avatarUrl}
                    alt="Avatar"
                    className="w-32 h-32 rounded-full object-cover border"
                />
                <h2 className="mt-4 text-xl font-semibold text-gray-800">{name}</h2>
            </div>

            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <div className="w-full border rounded-lg px-3 py-2 bg-gray-100 text-gray-800">
                    {email}
                </div>
            </div>
        </div>
    );
};

export default Profile;