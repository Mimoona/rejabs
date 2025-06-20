import CardDialog from "./CardDialog.tsx";
import { useState} from "react";
import {useCard} from "../hooks/useCard.ts";
import {PlusIcon} from "@heroicons/react/20/solid";
import CardItem from "./CardItem.tsx";
import CardPreview from "./CardPreview.tsx";
import type {Card} from "../types/Card.ts";


const CardList = ({listId}: { listId: string }) => {
    const [isCardDialogOpen, setIsCardDialogOpen] = useState(false);
    const [selectedCard, setSelectedCard] = useState<Card>();
    const {cards} = useCard();

    // Filter cards by this list
    const filteredCards = (cards || []).filter(card => card.listId === listId);
    console.log(listId);
    if (!listId || !cards) {
        return <div>Loading board...</div>;
    }


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