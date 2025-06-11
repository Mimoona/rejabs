import {useLocation} from "react-router-dom";
import {useState} from "react";
import CreateBoardDialog from "../component/CreateBoardDialog.tsx";
import {useBoard} from "../hooks/useBoard.ts";
import {UserIcon, UserPlusIcon} from "@heroicons/react/16/solid";
import type {Collaborator} from "../types/Board.ts";

const Boards = () => {
    const location = useLocation();
    const board = location.state?.board;
    const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
    const { updateBoard } = useBoard();
    const [isEditing, setIsEditing] = useState(false);
    if (!board) {
        return <div>Board not found</div>;
    }

    const handleTitleUpdate = async (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            const newTitle = e.currentTarget.value.trim();
            if (newTitle && newTitle !== board.title) {
                await updateBoard(board.boardId, { title: newTitle });
            }
            setIsEditing(false);
        }
    };


    return (

        <div className="flex flex-col h-screen w-full bg-slate-50">
            {/* Board Header */}
            <div className="bg-gray-500 shadow-md px-6 py-4 border-b rounded-lg">
                <div className="flex justify-between items-center">
                    {/* Board Name */}
                    <div className="relative flex-grow gap-1 ">
                        {isEditing ? (
                            <input
                                type="text"
                                defaultValue={board.title}
                                onKeyDown={handleTitleUpdate}
                                className="text-xl font-mono bg-transparent text-white border-b border-white/50 outline-none px-1 w-72"
                                autoFocus
                                minLength={30}
                                required
                            />
                        ) : (
                            <button
                                className="text-xl font-mono text-white cursor-pointer hover:bg-white/10 px-1 rounded"
                                onClick={() => setIsEditing(true)}
                            >
                                {board.title}
                            </button>
                        )}
                    </div>
                    <div className="flex items-center space-x-4">

                        {/* Collaborators */}

                        <div className="flex -space-x-2">

                            {board.collaborators?.length > 0 ? (
                                board.collaborators.map((collaborator:Collaborator) => (
                                    <img
                                        key={collaborator.collaboratorEmail}
                                        src={collaborator.collaboratorAvatar}
                                        alt={collaborator.collaboratorName}
                                        className="w-9 h-9 rounded-full border-2 border-white bg-gray-200"
                                        title={collaborator.collaboratorName}
                                    />
                                ))
                            ) : (
                                <div className="w-9 h-9 rounded-full border-2 border-white bg-gray-200 flex items-center justify-center">
                                    <UserIcon className="h-5 w-5 text-gray-500" />
                                </div>
                            )}
                        </div>

                        {/* Invite */}

                        <button
                            className=" bg-gray-700 text-white border rounded-full px-3 py-1 hover:bg-gray-800 text-sm"
                            onClick={() => {
                                setIsEditing(true)
                                setIsCreateDialogOpen(true)
                            }}
                        >
                            <UserPlusIcon className="h-6 w-6 text-white hover:text-gray-100"></UserPlusIcon>
                        </button>

                        <button
                            onClick={() => {
                                setIsEditing(false)
                                setIsCreateDialogOpen(true)
                            }}
                            className="bg-gray-700 hover:bg-gray-800 border text-white text-sm px-4 py-2 rounded-lg"
                        >
                            + Add Board
                        </button>

                        <CreateBoardDialog
                            isOpen={isCreateDialogOpen}
                            onClose={() => setIsCreateDialogOpen(false)}
                            board={board}
                            isEditing={isEditing}
                        />

                    </div>
                </div>
            </div>

            {/* Board Content */}

            <div
                className="bg-gradient-to-t from-gray-800 via-gray-500 to-gray-300 flex-1 overflow-x-auto px-6 py-4 rounded-lg mt-2">
                <div className="flex gap-4 w-max">
                    {/* List 1 */}
                    <div className="w-64 bg-white rounded-2xl shadow p-4 flex-shrink-0">
                        <h3 className="text-lg font-semibold text-gray-700 mb-3">List Name 1</h3>
                        <div className="space-y-3">
                            <div className="bg-gray-100 rounded-lg p-3 shadow-sm"> Card title</div>
                            <div className="bg-gray-100 rounded-lg p-3 shadow-sm"> Card title</div>
                        </div>
                    </div>

                    {/* List 2 */}
                    <div className="w-64 bg-white rounded-2xl shadow p-4 flex-shrink-0">
                        <h3 className="text-lg font-semibold text-gray-700 mb-3">List Name 2</h3>
                        <div className="space-y-3">
                            <div className="bg-gray-100 rounded-lg p-3 shadow-sm"> Card Title</div>
                        </div>
                    </div>

                    {/* Add list */}
                    <button
                        className="w-64 bg-indigo-100 text-indigo-700 rounded-2xl p-4 flex-shrink-0 hover:bg-indigo-200 transition">
                        + Add another list
                    </button>
                </div>
            </div>
        </div>

    );
};

export default Boards;