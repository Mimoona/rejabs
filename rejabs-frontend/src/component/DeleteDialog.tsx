import type {Board} from "../types/Board.ts";
import type {BoardList} from "../types/BoardList.ts";
import {Fragment} from "react";
import {Dialog, Transition} from "@headlessui/react";

interface DeleteDialogProps {
    isOpen: boolean;
    onClose: () => void;
    item?: Board | BoardList;
    onConfirm: () => void;
}
const DeleteDialog = ({isOpen, onClose, item, onConfirm}: DeleteDialogProps)=>{

    const itemTitle = (item && 'title' in item)
        ? item.title
        : item && 'listTitle' in item
            ? item.listTitle
            : 'this item';


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
                    <div className="fixed inset-0 bg-black/25" />
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
                        <Dialog.Panel className="w-full max-w-sm transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                            <Dialog.Title className="text-lg font-medium text-gray-900">
                                Delete {itemTitle}?
                            </Dialog.Title>
                            <div className="mt-2 text-sm text-gray-600">
                                Are you sure you want to delete <b>{itemTitle}</b>? This action
                                cannot be undone.
                            </div>

                            <div className="mt-4 flex justify-end space-x-2">
                                <button
                                    className="px-3 py-1 text-sm rounded-md bg-gray-200 hover:bg-gray-300"
                                    onClick={onClose}
                                >
                                    Cancel
                                </button>
                                <button
                                    className="px-3 py-1 text-sm rounded-md bg-red-600 text-white hover:bg-red-700"
                                    onClick={onConfirm}
                                >
                                    Delete
                                </button>
                            </div>
                        </Dialog.Panel>
                    </Transition.Child>
                </div>
            </Dialog>
        </Transition>


    )

}
export default DeleteDialog;