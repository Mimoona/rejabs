import type {Collaborator} from "../types/Board.ts";
import {UserIcon, UserPlusIcon} from "@heroicons/react/16/solid";
import CreateBoardDialog from "../component/CreateBoardDialog.tsx";
import {useState} from "react";

const Home = () => {

    return (
        <div className="flex flex-col h-screen w-full bg-slate-50">
            <h1> welcome to home Page</h1>

            TODO: planning to display dashboard for overview as due tasks as whole or may be as per board in cards display..let see


            {/*/!* Board Header *!/*/}
            {/*<div className="bg-gray-500 shadow-md px-6 py-4 border-b rounded-lg">*/}
            {/*    <div className="flex justify-between items-center">*/}
            {/*        /!* Board Name *!/*/}
            {/*        <h1 className="text-xl font-bold text-white">Board Name</h1>*/}
            {/*        <div className="flex items-center space-x-4">*/}
            {/*            /!* Collaborators *!/*/}
            {/*            <div className="flex -space-x-2">*/}
            {/*                <img src="/avatar1.png" alt="User 1"*/}
            {/*                     className="w-9 h-9 rounded-full border-2 border-white"/>*/}
            {/*                <img src="/avatar2.png" alt="User 2"*/}
            {/*                     className="w-9 h-9 rounded-full border-2 border-white"/>*/}
            {/*            </div>*/}
            {/*            <button*/}
            {/*                className="bg-gray-700 hover:bg-gray-800 border text-white text-sm px-4 py-2 rounded-lg"*/}
            {/*            >*/}
            {/*                + Add Board*/}
            {/*            </button>*/}

            {/*        </div>*/}
            {/*    </div>*/}
            {/*</div>*/}

            {/*/!* Board Content *!/*/}
            {/*<div*/}
            {/*    className="bg-gradient-to-t from-gray-800 via-gray-500 to-gray-300 flex-1 overflow-x-auto px-6 py-4 rounded-lg mt-2">*/}
            {/*    <div className="flex gap-4 w-max">*/}
            {/*        /!* List 1 *!/*/}
            {/*        <div className="w-64 bg-white rounded-2xl shadow p-4 flex-shrink-0">*/}
            {/*            <h3 className="text-lg font-semibold text-gray-700 mb-3">Todo</h3>*/}
            {/*            <div className="space-y-3">*/}
            {/*                <div className="bg-gray-100 rounded-lg p-3 shadow-sm"> Task 1</div>*/}
            {/*                <div className="bg-gray-100 rounded-lg p-3 shadow-sm"> Task 2</div>*/}
            {/*            </div>*/}
            {/*        </div>*/}

            {/*        /!* Add another list *!/*/}
            {/*        <button*/}
            {/*            className="w-64 bg-indigo-100 text-indigo-700 rounded-2xl p-4 flex-shrink-0 hover:bg-indigo-200 transition">*/}
            {/*            + Add another list*/}
            {/*        </button>*/}
            {/*    </div>*/}
            {/*</div>*/}
        </div>
)
}

export default Home;