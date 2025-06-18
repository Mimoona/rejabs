import type {Card} from "../types/Card.ts";
import {Fragment, useState} from "react";
import {Dialog, Transition} from "@headlessui/react";

interface CardDialogProps {
    listId: string;
    onCreate: (card: Partial<Card>) => void
    isOpen: boolean;
    onClose: () => void;
}

const CardDialog = ({listId, onCreate, isOpen, onClose}: CardDialogProps) => {
    const [cardTitle, setCardTitle] = useState("");
    const [description, setDescription] = useState("");
    const [labels, setLabels] = useState<string[]>([]);
    const [dueDate, setDueDate] = useState("");

    const handleSubmit = () => {
        if (!cardTitle.trim()) return;

        onCreate({ cardTitle, description, dueDate, labels });
        setCardTitle("");
        setDescription("");
        setDueDate("");
        setLabels([]);
        onClose();
    };


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
                    <div className="fixed inset-0 bg-black/30" />
                </Transition.Child>

                <div className="fixed inset-0 flex items-center justify-center p-4">
                    <Transition.Child
                        as={Fragment}
                        enter="ease-out duration-300"
                        enterFrom="opacity-0 scale-95"
                        enterTo="opacity-100 scale-100"
                        leave="ease-in duration-200"
                        leaveFrom="opacity-100 scale-100"
                        leaveTo="opacity-0 scale-95"
                    >
                        <Dialog.Panel className="w-full max-w-md rounded-xl bg-white p-6 shadow-xl">
                            <Dialog.Title className="text-lg font-semibold text-gray-800 mb-4">
                                Create New Card
                            </Dialog.Title>

                            <div className="space-y-4">
                                <input
                                    type="text"
                                    placeholder="Card title"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    value={cardTitle}
                                    onChange={(e) => setCardTitle(e.target.value)}
                                />

                                <textarea
                                    placeholder="Description"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    rows={3}
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />

                                <input
                                    type="date"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    value={dueDate}
                                    onChange={(e) => setDueDate(e.target.value)}
                                />

                                <input
                                    type="text"
                                    placeholder="Comma-separated labels"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    onChange={(e) => setLabels(e.target.value.split(",").map(l => l.trim()))}
                                />
                            </div>

                            <div className="mt-6 flex justify-end gap-3">
                                <button
                                    className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
                                    onClick={onClose}
                                >
                                    Cancel
                                </button>
                                <button
                                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                                    onClick={handleSubmit}
                                >
                                    Create
                                </button>
                            </div>
                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </Dialog>
        </Transition>
    )

}
export default CardDialog;