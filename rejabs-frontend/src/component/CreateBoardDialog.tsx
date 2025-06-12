import {Fragment, useEffect, useState} from "react";
import {Dialog, Transition} from '@headlessui/react';
import {useBoard} from "../hooks/useBoard.ts";
import {XMarkIcon} from "@heroicons/react/16/solid";
import type {Board, Collaborator} from "../types/Board.ts";


interface CreateBoardDialogProps {
    isOpen: boolean;
    onClose: () => void;
    board?: Board;
    isEditing?: boolean;
}

const CreateBoardDialog = ({isOpen, onClose, board, isEditing}: CreateBoardDialogProps) => {

    const {createBoard, updateBoard, refreshBoards} = useBoard();
    const [title, setTitle] = useState('');
    const [collaboratorName, setCollaboratorName] = useState('');
    const [collaboratorEmail, setCollaboratorEmail] = useState('');
    const [collaboratorAvatar, setCollaboratorAvatar] = useState('');
    const [collaborators, setCollaborators] = useState<Collaborator[]>([]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            if (isEditing && board) {
                await updateBoard(board.boardId, {collaborators});
                resetForm();
            } else {
                await createBoard({title, collaborators});
                resetForm();
            }
            refreshBoards();
            onClose();

        } catch (error) {
            console.error('Error:', error);
        }
    };

    const addCollaborator = () => {

        if (!collaboratorEmail.trim()) {
            return;
        }

        if (collaboratorEmail && /\S+@\S+\.\S+/.test(collaboratorEmail)) {
            if (collaborators.some(c => c.collaboratorEmail === collaboratorEmail)) {
                alert('This collaborator has already been added');
                return;
            }

            const newCollaborator: Collaborator = {
                collaboratorName: collaboratorName,
                collaboratorEmail: collaboratorEmail,
                collaboratorAvatar: collaboratorAvatar
            };

            setCollaborators([...collaborators, newCollaborator]);
            setCollaboratorEmail('');
            setCollaboratorName('');
            setCollaboratorAvatar('');
        }
    }
    const removeCollaborator = async (email: string) => {
        setCollaborators(collaborators.filter(c => c.collaboratorEmail !== email));
    };
    const resetForm = () => {
        setTitle('');
        setCollaboratorEmail('');
        setCollaboratorName('');
        setCollaborators([]);
    };


    useEffect(() => {
        if (board && isEditing) {
            setTitle(board.title);
            setCollaborators(board.collaborators || []);
        }
    }, [board, isEditing]);


    return (
        <Transition appear show={isOpen} as={Fragment}>
            <Dialog as="div" className="relative z-50" onClose={onClose}>
                <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <div className="fixed inset-0 bg-black/25"/>
                </Transition.Child>

                <div className="fixed inset-0">
                    <div className="flex min-h-full items-start justify-end p-4 mr-2 mt-[10rem]">
                        <Transition.Child
                            as={Fragment}
                            enter="ease-out duration-300"
                            enterFrom="opacity-0 translate-x-full"
                            enterTo="opacity-100 translate-x-0"
                            leave="ease-in duration-200"
                            leaveFrom="opacity-100 translate-x-0"
                            leaveTo="opacity-0 translate-x-full"
                        >
                            <Dialog.Panel
                                className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 shadow-xl transition-all">
                                <div className="flex justify-between items-center mb-4">
                                    <Dialog.Title className="text-lg font-medium text-gray-900">
                                        {isEditing ? 'Update Collaborators' : 'Create New Board'}
                                    </Dialog.Title>
                                    <button
                                        onClick={onClose}
                                        className="text-gray-400 hover:text-gray-500"
                                    >
                                        <XMarkIcon className="h-6 w-6"/>
                                    </button>
                                </div>

                                <form onSubmit={handleSubmit} className="space-y-4">
                                    {isEditing ? null : (
                                        <div>
                                            <label htmlFor="title" className="block text-sm font-medium text-gray-700">
                                                Board Title
                                            </label>

                                            <input
                                                id="title"
                                                type="text"
                                                value={title}
                                                onChange={(e) => setTitle(e.target.value)}
                                                className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                                placeholder="Enter board title"
                                                required
                                            />
                                        </div>
                                    )}
                                    <div>
                                        <label htmlFor="collaborator"
                                               className="block text-sm font-medium text-gray-700">
                                            Add Collaborators
                                        </label>
                                        <div className="mt-1 flex flex-col gap-2">
                                            <input
                                                id="name"
                                                type="name"
                                                value={collaboratorName}
                                                onChange={(e) => setCollaboratorName(e.target.value)}
                                                className="block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                                placeholder="Enter calloaborator name"
                                            />
                                            <input
                                                id="email"
                                                type="email"
                                                value={collaboratorEmail}
                                                onChange={(e) => setCollaboratorEmail(e.target.value)}
                                                className="block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                                placeholder="Enter email address"
                                            />
                                            <input
                                                id="avatar"
                                                type="avatar"
                                                value={collaboratorAvatar}
                                                onChange={(e) => setCollaboratorAvatar(e.target.value)}
                                                className="block w-full rounded-md border border-gray-300 px-3 py-2 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                                placeholder="Image url (later will add upload button optional)"
                                            />
                                            <button
                                                type="button"
                                                onClick={addCollaborator}
                                                disabled={!collaboratorEmail}
                                                className="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-gray-600 hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                                            >
                                                Add
                                            </button>

                                        </div>
                                    </div>

                                    {
                                        collaborators.length > 0 && (
                                            <div className="mt-4">
                                                <h4 className="text-sm font-medium text-gray-700">Collaborators</h4>
                                                <ul className="mt-2 space-y-1">
                                                    {collaborators.map((each) => (
                                                        <li key={each.collaboratorEmail}
                                                            className="flex items-center justify-between text-sm text-gray-700">
                                                            {each.collaboratorName}
                                                            <button
                                                                type="button"
                                                                onClick={() => removeCollaborator(each.collaboratorEmail)}
                                                                className="text-red-500 hover:text-red-700 focus:outline-none"
                                                            >
                                                                Remove
                                                            </button>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        )
                                    }

                                    <div className="mt-6">
                                        <button
                                            type="submit"
                                            className="w-full inline-flex justify-center rounded-md border border-transparent bg-gray-600 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
                                        >
                                            {isEditing ? 'Update' : 'Create Board'}
                                        </button>
                                    </div>
                                </form>
                            </Dialog.Panel>
                        </Transition.Child>
                    </div>
                </div>
            </Dialog>
        </Transition>
    )
}
export default CreateBoardDialog;
