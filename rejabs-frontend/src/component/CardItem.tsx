import {Dialog, Transition} from "@headlessui/react";
import {Fragment, useState} from "react";
import type {Card} from "../types/Card.ts";
import {getColorFromLabel} from "../utility/LabelColor.ts";
import {useCard} from "../hooks/useCard.ts";
import {LABEL_OPTIONS} from "../enums/Label.ts";

interface Props {
    card: Card;
    isOpen: boolean;
    onClose: () => void;
}

const CardItem = ({card, isOpen, onClose}: Props) => {

    const {updateCard, setError} = useCard();

    const [title, setTitle] = useState(card.cardTitle);
    const [description, setDescription] = useState(card.description);
    const [labels, setLabels] = useState<string[]>(card.labels);
    const [dueDate, setDueDate] = useState(card.dueDate);

    const handleLabelChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selected = Array.from(e.target.selectedOptions, option => option.value);
        setLabels(selected);
    };

    const handleUpdate = async () => {
        if (!title.trim()) {
            setError("Title is required");
            return;
        }

        const updatedCard: Partial<Card> = {
            cardTitle: title.trim(),
            description: description.trim(),
            dueDate,
            labels,
        };

        await updateCard(card.cardId, updatedCard);
        onClose();
    };

    return (


        <Transition appear show={isOpen} as={Fragment}>
            <Dialog as="div" className="relative z-50" onClose={onClose}>
                <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-200"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in duration-150"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <div className="fixed inset-0 bg-black/30"/>
                </Transition.Child>

                <div className="fixed inset-0 flex items-center justify-center p-4">
                    <Transition.Child
                        as={Fragment}
                        enter="ease-out duration-200"
                        enterFrom="opacity-0 scale-95"
                        enterTo="opacity-100 scale-100"
                        leave="ease-in duration-150"
                        leaveFrom="opacity-100 scale-100"
                        leaveTo="opacity-0 scale-95"
                    >
                        <Dialog.Panel className="w-full max-w-lg bg-white rounded-xl p-6 shadow-lg space-y-4">
                            <Dialog.Title className="text-xl font-semibold text-gray-800">
                                <input
                                    type="text"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                />
                            </Dialog.Title>

                            <textarea
                                className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                rows={4}
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                            />

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Due Date</label>
                                <input
                                    type="date"
                                    value={dueDate}
                                    onChange={(e) => setDueDate(e.target.value)}
                                    className="w-full border border-gray-300 rounded px-3 py-2"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Labels</label>
                                <select
                                    multiple
                                    value={labels}
                                    onChange={handleLabelChange}
                                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-blue-200"
                                >
                                    {LABEL_OPTIONS.map((label) => (
                                        <option key={label.name} value={label.name}>
                                            {label.name.replace("_", " ")}
                                        </option>
                                    ))}
                                </select>

                                <div className="flex flex-wrap gap-2 mt-2">
                                    {labels.map((label) => {
                                        const color = getColorFromLabel(label);
                                        return (
                                            <span
                                                key={label}
                                                className="px-2 py-1 text-xs rounded-full text-white"
                                                style={{backgroundColor: color}}
                                            >
                        {label.replace("_", " ")}
                      </span>
                                        );
                                    })}
                                </div>
                            </div>

                            <div className="text-xs text-gray-400">
                                Created at: {new Date(card.createdAt).toLocaleDateString()}
                            </div>

                            <div className="text-xs text-gray-400">
                                Updated at: {new Date(card.updatedAt).toLocaleDateString()}
                            </div>

                            <div className="flex justify-end gap-3 pt-4">
                                <button
                                    className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
                                    onClick={onClose}
                                >
                                    Close
                                </button>
                                <button
                                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                                    onClick={handleUpdate}
                                >
                                    Update
                                </button>
                            </div>
                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </Dialog>
        </Transition>
    );
};

export default CardItem;