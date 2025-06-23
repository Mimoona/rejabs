import CardDialog from "./CardDialog.tsx";
import {useState} from "react";
import {useCard} from "../hooks/useCard.ts";
import {PlusIcon} from "@heroicons/react/20/solid";
import CardItem from "./CardItem.tsx";
import CardPreview from "./CardPreview.tsx";
import type {Card} from "../types/Card.ts";
import DeleteDialog from "./DeleteDialog.tsx";
import {TrashIcon} from "@heroicons/react/24/solid";


const CardList = ({listId}: { listId: string }) => {
    const [isCardDialogOpen, setIsCardDialogOpen] = useState(false);
    const [selectedCard, setSelectedCard] = useState<Card | null>();
    const {cards, deleteCard} = useCard();
    const [cardToDelete, setCardToDelete] = useState<Card | null>();
    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

    // Filter cards by this list
    const filteredCards = (cards || []).filter(card => card.listId === listId);
    console.log(listId);
    if (!listId || !cards) {
        return <div>Loading board...</div>;
    }

    const handleDropOnDelete = (e) => {
        e.preventDefault();
        const cardId = e.dataTransfer.getData("card-id");
        const card = cards.find((c) => c.cardId === cardId);
        if (card) {
            setCardToDelete(card);
            setIsDeleteDialogOpen(true); // open confirmation dialog
        }
    };
    const handleConfirmDelete = async() => {
        if (cardToDelete) {
            await deleteCard(cardToDelete.cardId);
            setIsDeleteDialogOpen(false);
            setCardToDelete(null);
        }
    };
    const handleCloseDeleteDialog = () => {
        setIsDeleteDialogOpen(false);     // Close the dialog
        setCardToDelete(null);            // Clear the item
    };


    return (

        <div className="flex-grow space-y-3">
            {/* All existing Cards */}
            {filteredCards && filteredCards.map(card => (
                <div key={card.cardId}>
                    <CardPreview
                        key={card.cardId}
                        card={card}
                        onClick={() => setSelectedCard(card)}
                    />

                    {selectedCard && (
                        <CardItem
                            card={selectedCard}
                            isOpen={!!selectedCard}
                            onClose={() => setSelectedCard(null)}
                        />
                    )}
                </div>
            ))}

            {/* Add Card Button */}
            <button
                onClick={() => setIsCardDialogOpen(true)}
                className="w-full flex items-center justify-center bg-blue-100 hover:bg-blue-200 text-blue-700 py-1 px-2 rounded-md text-sm"
            >
                <PlusIcon className="h-4 w-4 mr-1"/>
                Add Card
            </button>
            <button
                onDragOver={(e) => e.preventDefault()}
                onDrop={handleDropOnDelete}
                type="button"
                className="fixed bottom-6 right-6 w-16 h-16 bg-red-600 hover:bg-red-700 text-white flex items-center justify-center rounded-full shadow-lg z-50"
            >
                <TrashIcon className="w-6 h-6"/>
            </button>
            <DeleteDialog
                isOpen={isDeleteDialogOpen}
                item={cardToDelete as Card}
                onClose={handleCloseDeleteDialog}
                onConfirm={handleConfirmDelete}
            />

            {/* Dialog to create card */}
            <CardDialog
                listId={listId}
                cards={filteredCards}
                isOpen={isCardDialogOpen}
                onClose={() => setIsCardDialogOpen(false)}
            />
        </div>
    )
};
export default CardList;