import {Link, Outlet, useNavigate} from "react-router-dom";
import {useAuth} from "../features/auth/AuthContext.tsx";

const Layout = () => {
    const {user, setUser, logout} = useAuth();
    const navigate = useNavigate();
    const handleLogout = async () => {
        await logout();
        setUser(null);
        navigate("/");
    };

    return (
        <div className="flex flex-col min-h-screen">
            {/* Navbar */}
            <header className="bg-gray-500 text-gray-50 py-4 px-6 shadow-md">
                <div className="max-w-7xl mx-auto flex justify-between items-center">
                    <nav className="space-x-4">
                        <Link to="/boards" className="hover:underline">Boards</Link>
                        <Link to="/profile" className="hover:underline">Profile</Link>
                    </nav>
                    <div className="flex items-center space-x-4">
                        {
                            user
                                ? <span className="ml-4">{user.login}</span>
                                : user === null && <Link to="/signup" className="hover:underline">Signup</Link>
                        }
                        <button onClick={handleLogout} className="hover:underline">(Logout)</button>
                    </div>
                </div>
            </header>

            {/* Main content area for nested routes */}
            <main className="flex-1 bg-white p-4">
                <div className="max-w-7xl mx-auto">
                    <Outlet/>
                </div>
            </main>

            {/* Footer */}
            <footer className="bg-gray-500 text-gray-50 py-3 px-6 text-center">
                &copy; {new Date().getFullYear()} ReJaBs.
            </footer>
        </div>
    );
};

export default Layout;