import { useState } from 'react';
import {useAuth} from "../hooks/useAuth.ts";

const Profile = () => {
    const {user} = useAuth();
    const [formData, setFormData] = useState({
        name: user?.login,
        email: user?.email?? "Not Available",
        avatarUrl: user?.avatar_url,
    });


    const handleSubmit = (e) => {
        e.preventDefault();
    };



    return (
        <form onSubmit={handleSubmit} className="max-w-md mx-auto mt-10 p-6 bg-white rounded-2xl shadow-md">
            <div className="flex flex-col items-center mb-6">
                <img
                    src={formData.avatarUrl?? '/default-avatar.png'}
                    alt="Avatar"
                    className="w-32 h-32 rounded-full object-cover border"
                />
            </div>

            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input
                    type="text"
                    name="name"
                    placeholder={formData.name}
                    value={formData.name}
                    readOnly
                    className="w-full border rounded-lg px-3 py-2"
                />
            </div>

            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input
                    type="email"
                    name="email"
                    placeholder={formData.email}
                    value={formData.email}
                    readOnly
                    className="w-full border rounded-lg px-3 py-2"
                />
            </div>

            <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-1">Avatar URL</label>
                <input
                    type="url"
                    name="avatarUrl"
                    value={formData.avatarUrl}
                    readOnly
                    className="w-full border rounded-lg px-3 py-2"
                />
            </div>

            <button
                type="submit"
                disabled
                className="w-full bg-gray-500 text-gray-50 py-2 px-4 rounded-lg hover:bg-gray-700"
            >
                Save
            </button>
        </form>
    );
};

export default Profile;