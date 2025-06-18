import CardDialog from "./CardDialog.tsx";
import {useState} from "react";

interface CardListProps {
    listId: string;
}

const CardList = ({listId}: CardListProps) => {
    // for now Card code
    const [isCardDialogOpen, setIsCardDialogOpen] = useState(false);

    return (
        <>
            {/*cardTitle*/}

            {/*description*/}

            {/*labels (e.g. array of strings or multiselect input)*/}

            {/*dueDate (date picker)*/}
            <CardDialog
                isOpen={isCardDialogOpen}
                onClose={() => setIsCardDialogOpen(false)}
                onCreate={(data) => {
                    createCard({ ...data, listId, position: existingCards.length });
                }}
            />


        </>
    )
}
export default CardList;