import {useBoardList} from "../hooks/useBoardList.ts";
import {useParams} from "react-router-dom";
import {useMemo, useRef, useState} from "react";
import type {BoardList} from "../types/BoardList.ts";
import {TrashIcon} from "@heroicons/react/24/outline";
import DeleteDialog from "./DeleteDialog.tsx";
import CardList from "./CardList.tsx";

export default function BoardList() {
    const {boardId} = useParams<{ boardId: string }>();
    const {
        boardLists,
        createBoardList,
        updateBoardList,
        deleteBoardList,
        error,
        setError
    } = useBoardList();
    const [isAdding, setIsAdding] = useState<boolean>(false);
    const [listTitle, setListTitle] = useState("");
    const [editingListId, setEditingListId] = useState<string | null>(null);
    const scrollRef = useRef<HTMLDivElement>(null);
    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
    const [selectedList, setSelectedList] = useState<BoardList | null>(null);

    // Filter and sort lists for current board
    const currentBoardLists = useMemo(() => {
        return boardLists
            ?.filter(list => list.boardId === boardId)
            ?.sort((a, b) => (a.position ?? 0) - (b.position ?? 0)) || [];
    }, [boardLists, boardId]);


    if (!boardId) {
        return <div>Loading board...</div>;
    }

    // Create BoardList

    const handleCreateBoardList = async (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            const newListTitle = e.currentTarget.value.trim();

            if (!newListTitle) {
                setError('Please enter a list name');
                return;
            }

            const newList: Partial<BoardList> = {
                listTitle: newListTitle,
                boardId,
                position: currentBoardLists?.length
            };

            const createdList: BoardList | null = await createBoardList(newList);
            if (createdList && createdList.boardId === boardId) {
                //await refreshBoardLists();
                setListTitle('');
                setIsAdding(false);
                setError("");
                setTimeout(() => {
                    scrollRef.current?.scrollTo({left: scrollRef.current.scrollWidth, behavior: "smooth"});
                }, 100); // slight delay to allow rendering
            }
        }
        if (e.key === 'Escape') {
            setListTitle('');
            setIsAdding(false);
            setError('');
        }
    }

    // Update

    const handleUpdateListTitle = async (list: BoardList, title: string) => {
        if (!title.trim()) {
            setEditingListId(null);
            return;
        }

        // First, make the API call
        await updateBoardList(list.boardListId, {
            listTitle: title.trim(),
            boardId,
            position: list.position
        });

        setEditingListId(null);
    };

    // Delete List

    const openDeleteDialog = (list: BoardList) => {
        setSelectedList(list);
        setIsDeleteDialogOpen(true);
    };

    const handleConfirmDelete = async () => {
        if (selectedList) {
            await deleteBoardList(selectedList.boardListId);
            setIsDeleteDialogOpen(false);
            setSelectedList(null);
        }
    };


    // For list scroll controls
    const scroll = (direction: "left" | "right") => {
        if (!scrollRef.current) return;
        const scrollAmount = 375; // px
        scrollRef.current.scrollBy({
            left: direction === "right" ? scrollAmount : -scrollAmount,
            behavior: "smooth",
        });
    };


    return (

        <div className="flex-1 overflow-hidden relative">
            {/* Scroll Buttons */}
            <div className="flex items-center">
                <button
                    className="absolute left-0 top-1/2 transform -translate-y-1/2 z-1000 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                    onClick={() => scroll("left")}
                >
                    ←
                </button>
                <button
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 z-1000 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                    onClick={() => scroll("right")}
                >
                    →
                </button>
            </div>


            {/* Scrollable list container */}
            <div
                ref={scrollRef}
                className="flex gap-4 w-full left-0 overflow-x-auto pb-4 mx-8 scroll-smooth hide-scrollbar items-start"
                style={{scrollBehavior: "smooth"}}
            >


                {currentBoardLists.map(list => (
                    <div
                        key={list.boardListId}
                        className="min-w-[20rem] w-80 bg-white rounded-2xl shadow p-4 flex flex-col"
                    >
                        <div className="flex items-center justify-between w-full mb-3">

                            {editingListId === list.boardListId ? (
                                <input
                                    type="text"
                                    defaultValue={list.listTitle}
                                    autoFocus
                                    className="w-full p-2 text-lg font-semibold text-gray-700 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                                    onBlur={(e) => handleUpdateListTitle(list, e.target.value)}
                                    onKeyDown={(e) => {
                                        if (e.key === "Enter") handleUpdateListTitle(list, e.currentTarget.value);
                                        else if (e.key === "Escape") setEditingListId(null);
                                    }}
                                />
                            ) : (
                                <button
                                    className="text-lg font-semibold text-gray-700 cursor-pointer hover:bg-gray-100 rounded px-2 truncate w-full text-left"
                                    title={list.listTitle}
                                    onClick={() => setEditingListId(list.boardListId)}
                                >
                                    {list.listTitle}
                                </button>
                            )}
                            <button onClick={() => openDeleteDialog(list)}>
                                <TrashIcon className="h-5 w-5 text-gray-500 hover:text-red-600"></TrashIcon>
                            </button>
                            <DeleteDialog isOpen={isDeleteDialogOpen}
                                          onClose={() => setIsDeleteDialogOpen(false)}
                                          item={list}
                                          onConfirm={handleConfirmDelete}
                            ></DeleteDialog>
                        </div>
                        <CardList listId={list.boardListId}/>
                    </div>
                ))}

                {/* Add New List UI*/}

                {isAdding ? (
                    <div className="w-64 flex-shrink-0 bg-white rounded-2xl shadow p-4">
                        <input
                            autoFocus
                            value={listTitle}
                            placeholder="Enter list title..."
                            className={`w-full p-2 border rounded focus:outline-none ${
                                error ? "border-red-500" : "border-gray-300"
                            }`}
                            onChange={(e) => {
                                setListTitle(e.target.value);
                                if (error) setError("");
                            }}
                            onKeyDown={handleCreateBoardList}
                            onBlur={() => {
                                if (!listTitle.trim()) {
                                    setIsAdding(false);
                                    setError("");
                                }
                            }}
                        />
                        {error && <p className="text-red-500 text-sm mt-1">{error}</p>}
                        <p className="text-sm text-gray-500 mt-2">Press Enter to add • Esc to cancel</p>
                    </div>
                ) : (
                    <button
                        onClick={() => {
                            setIsAdding(true);
                            setListTitle("");
                            setEditingListId(null);
                        }}
                        className="w-64 bg-indigo-100 text-blue-700 rounded-2xl p-4 hover:bg-indigo-200 transition"
                    >
                        + Add another list
                    </button>
                )}
            </div>
        </div>
    )
};