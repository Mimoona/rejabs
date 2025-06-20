import {useParams} from "react-router-dom";
import {useState} from "react";
import CreateBoardDialog from "../component/CreateBoardDialog.tsx";
import {useBoard} from "../hooks/useBoard.ts";
import {UserIcon, UserPlusIcon} from "@heroicons/react/16/solid";
import type {Collaborator} from "../types/Board.ts";
import BoardList from "../component/BoardList.tsx";
import {ExclamationCircleIcon} from "@heroicons/react/24/outline";

const Boards = () => {
    const {boardId} = useParams();
    const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
    const {boards, updateBoard, refreshBoards, error, setError} = useBoard();
    const [isEditing, setIsEditing] = useState(false);


    const board = boards.find(b => b.boardId === boardId);
    if (!board) {
        return <div>Board not found</div>;
    }

    const handleTitleUpdate = async (e: React.KeyboardEvent<HTMLInputElement>) => {
        setError("");

        if (e.key === 'Enter') {
            const newTitle = e.currentTarget.value.trim();

            if (!newTitle) {
                setError("Please enter a Board name");
                return;
            }
            if (newTitle && newTitle !== board.title) {
                await updateBoard(board.boardId, {title: newTitle});
                await refreshBoards();
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
                            <div className="flex items-center gap-2 relative">
                                <input
                                    type="text"
                                    defaultValue={board.title}
                                    onKeyDown={handleTitleUpdate}
                                    className={`text-xl font-mono bg-transparent text-white border-b ${error ? "border-red-500" : "border-white/50"} outline-none px-1 w-72`}
                                    autoFocus
                                />

                                {error && (
                                    <span className="text-red-500 text-sm"
                                          title={error}>
                                        <ExclamationCircleIcon className="h-6 w-6 text-red "></ExclamationCircleIcon>
                                    </span>
                                )}

                            </div>
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
                                board.collaborators.map((collaborator: Collaborator) => (
                                    <img
                                        key={collaborator.collaboratorEmail}
                                        src={collaborator.collaboratorAvatar || collaborator.collaboratorName}
                                        alt={collaborator.collaboratorName}
                                        className="w-9 h-9 rounded-full border-2 border-white bg-gray-200"
                                        title={collaborator.collaboratorName}
                                    />
                                ))
                            ) : (
                                <div
                                    className="w-9 h-9 rounded-full border-2 border-white bg-gray-200 flex items-center justify-center">
                                    <UserIcon className="h-5 w-5 text-gray-500"/>
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
                <BoardList/>
            </div>
        </div>

    );
};

export default Boards;
