import {Link} from "react-router-dom";

const LandingPage = () => {
    return (
        <div className="flex flex-col min-h-screen">
            <header className="bg-gray-500 text-gray-50 py-4 px-6 shadow-md">
                <div className="max-w-7xl mx-auto flex justify-between items-center">
                    <button className="hover:underline">ReJaBs</button>
                    <Link to="/signin" className="hover:underline">Sign-in</Link>
                </div>
            </header>
            <section className="flex flex-1 flex-col items-center justify-center text-center py-20 px-6">
                <h2 className="text-5xl md:text-5xl font-bold mb-4">Quick jabs at your tasks</h2>
                <h3 className="text-3xl md:text-5xl font-bold mb-4">
                    Organize your work and life with ease
                </h3>
                <p className="text-lg text-gray-600 max-w-xl mb-8">
                    A Trello-inspired productivity tool to manage boards, tasks, and teams — beautifully and simply.
                </p>
                <Link
                    to="/signup"
                    className="bg-blue-600 text-white px-6 py-3 rounded-lg text-lg hover:bg-blue-700 transition"
                >
                Try It
                </Link>
            </section>
            <footer className="bg-gray-500 text-gray-50 py-3 px-6 text-center">
                &copy; {new Date().getFullYear()} ReJaBs. All Rights Reserved.
            </footer>
        </div>
    );
};

export default LandingPage;