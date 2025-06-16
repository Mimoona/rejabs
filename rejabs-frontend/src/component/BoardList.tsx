import {useBoardList} from "../hooks/useBoardList.ts";
import {useParams} from "react-router-dom";
import {useMemo, useRef, useState} from "react";
import type {BoardList} from "../types/BoardList.ts";

export default function BoardList() {
    const {boardId} = useParams<{ boardId: string }>();
    const {
        boardLists,
        createBoardList,
        updateBoardList,
        error,
        setError
    } = useBoardList();
    const [isAdding, setIsAdding] = useState<boolean>(false);
    const [listTitle, setListTitle] = useState("");
    const [editingListId, setEditingListId] = useState<string | null>(null);
    const scrollRef = useRef<HTMLDivElement>(null);

    if (!boardId) {
        return <div>Loading board...</div>;
    }

    // Filter and sort lists for current board
    const currentBoardLists = useMemo(() => {
        return boardLists
            ?.filter(list => list.boardId === boardId)
            ?.sort((a, b) => (a.position ?? 0) - (b.position ?? 0)) || [];
    }, [boardLists, boardId]);

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

            const createdList: Partial<BoardList> = await createBoardList(newList);
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

    const handleUpdateListTitle = async (list: BoardList, value: string) => {
        if (!value.trim()) {
            setEditingListId(null);
            return;
        }

        // First, make the API call
        await updateBoardList(list.boardListId, {
            listTitle: value.trim(),
            boardId,
            position: list.position
        });

        setEditingListId(null);
    };

    // For list scroll controls
    const scroll = (direction: "left" | "right") => {
        if (!scrollRef.current) return;
        const scrollAmount = 300; // px
        scrollRef.current.scrollBy({
            left: direction === "right" ? scrollAmount : -scrollAmount,
            behavior: "smooth",
        });
    };


    return (

        <div className="flex-1 h-full overflow-y-auto">
            {/* Scroll Buttons */}
            <button
                className="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                onClick={() => scroll("left")}
            >
                ←
            </button>
            <button
                className="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                onClick={() => scroll("right")}
            >
                →
            </button>

            {/* Scrollable list container */}
            <div
                ref={scrollRef}
                className="flex gap-4 w-full overflow-x-auto pb-4 scroll-smooth hide-scrollbar"
                style={{scrollBehavior: "smooth"}}
            >
                {currentBoardLists.map(list => (
                    <div
                        key={list.boardListId}
                        className="w-64 flex-shrink-0 bg-white rounded-2xl shadow p-4"
                    >

                        {editingListId === list.boardListId ? (
                            <input
                                type="text"
                                defaultValue={list.listTitle}
                                autoFocus
                                className="w-full p-2 text-lg font-semibold text-gray-700 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
                                onBlur={(e) => handleUpdateListTitle(list, e)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") handleUpdateListTitle(list, e.currentTarget.value);
                                    else if (e.key === "Escape") setEditingListId(null);
                                }}
                            />
                        ) : (
                            <button
                                className="text-lg font-semibold text-gray-700 mb-3 cursor-pointer hover:bg-gray-100 rounded px-2 py-1"
                                onClick={() => setEditingListId(list.boardListId)}
                            >
                                {list.listTitle}
                            </button>
                        )}

                        <div className="space-y-3">
                            <div className="bg-gray-100 rounded-lg p-3 shadow-sm">Task 1</div>
                        </div>
                        <button className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700">
                            Add Card
                        </button>
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
                        className="w-64 flex-shrink-0 bg-indigo-100 text-indigo-700 rounded-2xl p-4 hover:bg-indigo-200 transition"
                    >
                        + Add another list
                    </button>
                )}
            </div>
        </div>
    )
};