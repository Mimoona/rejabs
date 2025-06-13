import {useBoardList} from "../hooks/useBoardList.ts";
import {useParams} from "react-router-dom";
import {useEffect, useMemo, useRef, useState} from "react";
import type {BoardList} from "../types/BoardList.ts";

export default function BoardList() {
    const {boardId} = useParams<{ boardId: string }>();
    const {boardLists, createBoardList} = useBoardList();
    const [isAdding, setIsAdding] = useState<boolean>(false);
    const [listTitle, setListTitle] = useState("");
    const [isEditing, setIsEditing] = useState(false);
    const [error, setError] = useState('');
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

            try {
                const newList: Partial<BoardList> = {
                    listTitle: newListTitle,
                    boardId: boardId,
                    position: currentBoardLists?.length || 0
                };

                const createdList:Partial<BoardList> = await createBoardList(newList);
                if (createdList && createdList.boardId === boardId) {
                    setListTitle('');
                    setIsAdding(false);
                    setError('');
                }
            } catch (err) {
                setError( err);
            }
        }
        if (e.key === 'Escape') {
            setListTitle('');
            setIsAdding(false);
            setError('');
        }
    }

    // For list scroll
    const scroll = (direction: "left" | "right") => {
        if (!scrollRef.current) return;
        const scrollAmount = 300; // px
        scrollRef.current.scrollBy({
            left: direction === "right" ? scrollAmount : -scrollAmount,
            behavior: "smooth",
        });
    };

    useEffect(() => {
        if (scrollRef.current) {
            scrollRef.current.scrollTo({
                left: scrollRef.current.scrollWidth,
                behavior: "smooth",
            });
        }
    }, [boardLists.length]);


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

            {/* Scrollable List Row */}
            <div
                ref={scrollRef}
                className="flex gap-4 w-full overflow-x-auto pb-4 scroll-smooth"
                style={{scrollBehavior: "smooth"}}
            >
                {currentBoardLists.map(list => (
                    <div
                        key={list.boardListId}
                        className="w-64 flex-shrink-0 bg-white rounded-2xl shadow p-4"
                    >
                        <h3 className="text-lg font-semibold text-gray-700 mb-3">
                            {list.listTitle}
                        </h3>
                        <div className="space-y-3">
                            <div className="bg-gray-100 rounded-lg p-3 shadow-sm">Task 1</div>
                        </div>
                        <button className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700">
                            Add Card
                        </button>
                    </div>
                ))}

                {/* Add New List */}
                {isAdding ? (
                    <div className="w-64 flex-shrink-0 bg-white rounded-2xl shadow p-4">
                        {/* Add list form input */}
                        {/* ... (same as your input logic) */}
                        <div className="relative">
                            {isEditing ? (
                                <div>
                                    <input
                                        className={`w-full p-2 border rounded focus:outline-none focus:border-blue-500 text-gray-700
                                ${error ? 'border-red-500' : 'border-gray-300'}`}
                                        type="text"
                                        placeholder="Enter list title..."
                                        autoFocus
                                        value={listTitle}
                                        onKeyDown={handleCreateBoardList}
                                        onBlur={() => {
                                            if (!listTitle.trim()) {
                                                setIsAdding(false);
                                                setError('');
                                            }
                                        }}
                                        onChange={(e) => {
                                            setListTitle(e.target.value);
                                            if (error) setError('');
                                        }}
                                    />
                                    {error && (
                                        <div className="absolute -bottom-6 left-0 text-red-500 text-sm">
                                            {error}
                                        </div>
                                    )}
                                    <div className="text-sm text-gray-500 mt-2">
                                        Press Enter to add • Esc to cancel
                                    </div>

                                </div>
                            ) : (
                                <button
                                    onClick={() => setIsEditing(true)}
                                    className="w-full text-left p-2 text-gray-700 hover:bg-gray-100 rounded transition-colors"
                                >
                                    {listTitle}
                                </button>
                            )}
                        </div>

                        <button
                            className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                        >
                            Add card
                        </button>


                    </div>
                ) : (
                    <button
                        onClick={() => {
                            setIsAdding(true);
                            setIsEditing(true);
                        }}
                        className="w-64 flex-shrink-0 bg-indigo-100 text-indigo-700 rounded-2xl p-4 hover:bg-indigo-200 transition"
                    >
                        + Add another list
                    </button>
                )}
            </div>
        </div>

    // <div className="flex gap-4 w-max">
    //     {/* Existing Lists */}
    //     {currentBoardLists?.map((list) => (
    //         <div
    //             key={list.boardListId}
    //             className="w-64 bg-white rounded-2xl shadow p-4 flex-shrink-0"
    //         >
    //             <h3 className="text-lg font-semibold text-gray-700 mb-3">
    //                 {list.listTitle}
    //             </h3>
    //             {/* Placeholder for cards */}
    //             <div className="space-y-3">
    //                 <div className="bg-gray-100 rounded-lg p-3 shadow-sm">Task 1</div>
    //             </div>
    //             <button
    //                 className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
    //             >
    //                 Add Card
    //             </button>
    //         </div>
    //     ))}
    //
    //     {/* Add List Form */}
    //     {isAdding ? (
    //         <div className="w-64 bg-white rounded-2xl shadow p-4 flex-shrink-0">
    //             <div className="relative">
    //                 {isEditing ? (
    //                     <div>
    //                         <input
    //                             className={`w-full p-2 border rounded focus:outline-none focus:border-blue-500 text-gray-700
    //                             ${error ? 'border-red-500' : 'border-gray-300'}`}
    //                             type="text"
    //                             placeholder="Enter list title..."
    //                             autoFocus
    //                             value={listTitle}
    //                             onKeyDown={handleCreateBoardList}
    //                             onBlur={() => {
    //                                 if (!listTitle.trim()) {
    //                                     setIsAdding(false);
    //                                         setError('');
    //                                     }
    //                                 }}
    //                                 onChange={(e) => {
    //                                     setListTitle(e.target.value);
    //                                     if (error) setError('');
    //                                 }}
    //                             />
    //                             {error && (
    //                                 <div className="absolute -bottom-6 left-0 text-red-500 text-sm">
    //                                     {error}
    //                                 </div>
    //                             )}
    //                             <div className="text-sm text-gray-500 mt-2">
    //                                 Press Enter to add • Esc to cancel
    //                             </div>
    //
    //                         </div>
    //                     ) : (
    //                         <button
    //                             onClick={() => setIsEditing(true)}
    //                             className="w-full text-left p-2 text-gray-700 hover:bg-gray-100 rounded transition-colors"
    //                         >
    //                             {listTitle}
    //                         </button>
    //                     )}
    //                 </div>
    //
    //                 <button
    //                     className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
    //                 >
    //                     Add card
    //                 </button>
    //
    //             </div>
    //         ) : (
    //             <button
    //                 onClick={() => {
    //                     setIsAdding(true);
    //                     setIsEditing(true);
    //                 }}
    //                 className="w-64 bg-indigo-100 text-indigo-700 rounded-2xl p-4 flex-shrink-0 hover:bg-indigo-200 transition"
    //             >
    //                 + Add another list
    //             </button>
    //         )}
    //     </div>
    )
};