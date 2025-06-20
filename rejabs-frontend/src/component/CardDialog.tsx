import type {Card} from "../types/Card.ts";
import {Fragment, useState} from "react";
import {Dialog, Transition} from "@headlessui/react";
import {LABEL_OPTIONS} from "../enums/Label";
import {useCard} from "../hooks/useCard.ts";

interface CardDialogProps {
    listId: string;
    cards: Card[];
    isOpen: boolean;
    onClose: () => void;
}

const CardDialog = ({listId, cards, isOpen, onClose}: CardDialogProps) => {
    const [cardTitle, setCardTitle] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [dueDate, setDueDate] = useState<string>("");
    const [selectedLabels, setSelectedLabels] = useState<string[]>([]);
    const {createCard, error, setError} = useCard();

    const handleCreateCard = async () => {
        if (!cardTitle.trim()) {
            setError("Card title is required");
            return;
        }

        const newCard: Partial<Card> = {
            cardTitle: cardTitle.trim(),
            description: description.trim(),
            listId,
            position: cards.length,
            labels: selectedLabels, // ["URGENT", "DESIGN"]
            dueDate: dueDate,
        };
        try {
            const createdCard: Card | null = await createCard(newCard);
            if (createdCard) {
                setCardTitle("");
                setDescription("");
                setSelectedLabels([]);
                setDueDate(null);
                onClose();
            }

        } catch (err: unknown) {
            setError("Failed to create card." + err);
        }
    };


    const handleLabelChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selected = Array.from(e.target.selectedOptions, (option) => option.value);
        console.log(selected);
        setSelectedLabels(selected);
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
                    <div className="fixed inset-0 bg-black/30"/>
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
                                {error && <p className="text-red-500 text-sm">{error}</p>}
                                <input
                                    type="text"
                                    placeholder="Card title"
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    value={cardTitle}
                                    onChange={(e) => setCardTitle(e.target.value)}
                                    required
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
                                    min={new Date().toISOString().split("T")[0]} // today's date onwards
                                    className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    value={dueDate}
                                    onChange={(e) => setDueDate(e.target.value)}
                                />

                                <label className="block text-sm font-medium text-gray-700 mb-1">Labels</label>
                                <select
                                    multiple
                                    value={selectedLabels}
                                    onChange={handleLabelChange}
                                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring focus:ring-blue-200"
                                >
                                    {LABEL_OPTIONS.map((label) => (
                                        <option key={label.name} value={label.name}>
                                            {label.name.replace("_", " ")}
                                        </option>
                                    ))}
                                </select>
                                <p className="text-xs text-gray-500 mt-1">Hold Ctrl (Windows) or Cmd (Mac) to select
                                    multiple
                                </p>

                                <div className="flex flex-wrap gap-2 mt-2">
                                    {selectedLabels.map(labelName => {
                                        const labelObj = LABEL_OPTIONS.find(l => l.name === labelName);
                                        return (
                                            <span
                                                key={labelName}
                                                className="px-2 py-1 text-xs rounded-full text-white"
                                                style={{backgroundColor: labelObj?.color || "gray"}}
                                            >
                                                {labelName.replace("_", " ")}
                                             </span>
                                        );
                                    })}
                                </div>
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
                                    onClick={handleCreateCard}
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