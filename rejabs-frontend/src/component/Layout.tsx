import {Link, Outlet, useNavigate} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import {ChevronDownIcon, UserCircleIcon} from "@heroicons/react/16/solid";
import {useAuth} from "../hooks/useAuth.ts";
import {useBoard} from "../hooks/useBoard.ts";

const Layout = () => {
    const {user, setUser, logout} = useAuth();
    const navigate = useNavigate();
    const [openUserMenu, setOpenUserMenu] = useState(false);
    const [openBoardsMenu, setOpenBoardsMenu] = useState(false);
    const menuRef = useRef<HTMLDivElement |null>(null); // For user menu
    const boardsMenuRef = useRef<HTMLDivElement|null>(null);

    const {boards} = useBoard();


    const handleLogout = () => {
        logout();
        setUser(null);
        navigate("/");
    };
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setOpenUserMenu(false);
            }

            if (boardsMenuRef.current && !boardsMenuRef.current.contains(event.target as Node)) {
                setOpenBoardsMenu(false);
            }
        }

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);


    return (
        <div className="flex flex-col min-h-screen">
            {/* Navbar */}
            <header className="bg-gray-800 text-gray-50 py-4 px-6 shadow-md">
                <div className="mx-auto flex flex-col sm:flex-row justify-between items-center gap-4">
                    <nav className="w-full sm:w-auto flex items-center">
                        <Link
                            to="/home"
                            className="ml-20"
                        >Dashboard</Link>
                        <div className="relative" ref={boardsMenuRef}>
                            <button
                                onClick={() => setOpenBoardsMenu(!openBoardsMenu)}
                                className="flex items-center gap-1 hover:text-gray-300 px-3 py-2 rounded-md"
                            >
                                My Boards
                                <ChevronDownIcon
                                    className={`h-4 w-4 text-white hover:text-gray-100 transition-transform duration-200 ${
                                        openBoardsMenu ? 'transform rotate-180' : ''
                                    }`}
                                />
                            </button>

                            {/* Dropdown Menu of My Boards */}
                            {openBoardsMenu && (
                                <div className="absolute left-0 mt-2 w-56 bg-white rounded-md shadow-lg z-50">
                                    <div className="py-1">
                                        {/* Mapping board List */}
                                        {boards && boards.length > 0 ? (
                                            boards.map((board, index) => (
                                                <Link
                                                    key={board.boardId + index}
                                                    to={`/boards/${board.boardId}`}
                                                    // state={{board: board}}
                                                    className="block px-4 py-2 text-gray-700 hover:bg-gray-100 hover:text-gray-900"
                                                    onClick={() => setOpenBoardsMenu(false)}
                                                >
                                                    {board.title}
                                                </Link>
                                            ))
                                        ) : (
                                            <div className="px-4 py-2 text-gray-700">
                                                No Boards Available
                                            </div>
                                        )}
                                    </div>
                                </div>
                            )}
                        </div>

                    </nav>
                    <div className="relative flex items-center space-x-4" ref={menuRef}>
                        {user ? (
                            <div className="relative">
                                <button
                                    onClick={() => setOpenUserMenu(!openUserMenu)}
                                    className="ml-4 px-3 py-1 rounded hover:bg-gray-600"
                                >
                                    <UserCircleIcon className="h-8 w-8 text-white hover:text-gray-100"/>
                                </button>

                                {openUserMenu && (
                                    <div
                                        className="absolute right-0 mt-2 w-40 bg-white text-gray-800 rounded shadow-lg z-50">
                                        <span className="block px-4 py-2 hover:bg-gray-100">{user.username}</span>
                                        <Link
                                            to="/profile"
                                            className="block px-4 py-2 hover:bg-gray-100"
                                            onClick={() => setOpenUserMenu(false)}
                                        >
                                            View Profile
                                        </Link>
                                        <button
                                            onClick={() => {
                                                handleLogout();
                                                setOpenUserMenu(false);
                                            }}
                                            className="w-full text-left px-4 py-2 hover:bg-gray-100"
                                        >
                                            Logout
                                        </button>
                                    </div>
                                )}
                            </div>
                        ) : (
                            user === null && (
                                <Link to="/signup" className="hover:underline">Signup</Link>
                            )
                        )}
                    </div>
                </div>
            </header>

            {/* Main content area for nested routes */}
            <main className="flex-1 bg-white p-4 overflow-y-auto">
                <Outlet/>
            </main>

            {/* Footer */}
            <footer className="bg-gray-800 text-gray-50 py-3 px-6 text-center">
                &copy; {new Date().getFullYear()} ReJaBs.
            </footer>
        </div>
    );
};

export default Layout;