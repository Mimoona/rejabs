import {Dialog, Transition} from "@headlessui/react";
import {Fragment} from "react";
import type {Card} from "../types/Card.ts";
import {getColorFromLabel} from "../utility/LabelColor.ts";

interface Props {
    card: Card;
    isOpen: boolean;
    onClose: () => void;
}

const CardItem = ({card, isOpen, onClose}: Props) => {

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
                                {card.cardTitle}
                            </Dialog.Title>

                            <p className="text-gray-600 whitespace-pre-wrap">{card.description}</p>

                            <div className="flex flex-wrap gap-2 mt-2">
                                {card.labels.length && card.labels.map((label) => (
                                    <span
                                        key={label}
                                        className="px-2 py-1 text-xs rounded-full text-white"
                                        style={{backgroundColor: getColorFromLabel(label)}}
                                    >
                                        {label.replace("_", " ")}
                                    </span>
                                ))}
                            </div>

                            <div>
                                <span className="text-sm text-gray-500">Due: {card.dueDate}</span>
                            </div>


                            <div className="text-xs text-gray-400">
                                Created at: {new Date(card.createdAt).toLocaleDateString()}
                            </div>


                            <div className="text-xs text-gray-400">
                                Updated at: {new Date(card.updatedAt).toLocaleDateString()}
                            </div>


                            <div className="flex justify-end">
                                <button
                                    className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
                                    onClick={onClose}
                                >
                                    Close
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