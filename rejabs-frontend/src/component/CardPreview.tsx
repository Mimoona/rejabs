import {CalendarIcon, PencilIcon} from "@heroicons/react/24/outline";
import type {Card} from "../types/Card.ts";
import {getColorFromLabel} from "../utility/LabelColor.ts";

interface Props {
    card: Card;
    onClick: () => void;
}

const CardPreview = ({card, onClick}: Props) => {


    const handleDragStart = (e: React.DragEvent) => {
        e.dataTransfer.effectAllowed = "move";

        // Set card as draggable item
        e.dataTransfer.setData("card-id", card.cardId);

        //  use the element itself as the drag image
        const dragGhost = e.currentTarget.cloneNode(true) as HTMLElement;
        dragGhost.style.position = "absolute";
        dragGhost.style.top = "-1000px"; // Move it out of view
        dragGhost.style.left = "-1000px";
        dragGhost.style.pointerEvents = "none";
        document.body.appendChild(dragGhost);

        // Set drag image
        e.dataTransfer.setDragImage(dragGhost, 0, 0);

        // Clean up after short delay
        setTimeout(() => document.body.removeChild(dragGhost), 0);
    };

    return (

        <div
            role="button"
            tabIndex={0}
            draggable
            onDragStart={handleDragStart}
            style={{ userSelect: "none" }}
            onClick={onClick}
            onKeyDown={(e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    onClick();
                }
            }}
            className="bg-gray-200 hover:bg-gray-300 rounded p-2 cursor-grab active:cursor-grabbing shadow-sm transition "
        >
            <div className="flex items-center justify-between">
                <h3 className="text-sm font-medium text-gray-800 truncate">{card.cardTitle}</h3>
                <PencilIcon className="h-4 w-4" onClick={onClick} draggable={false}></PencilIcon>
            </div>
            <div>
                <p className="text-sm text-gray-600 truncate my-4">{card.description}</p>
            </div>

            <div className="flex items-center gap-1 mt-2">
                {card.labels.map((label) => (
                    <span
                        key={label}
                        className="w-3 h-3 rounded-full"
                        style={{backgroundColor: getColorFromLabel(label)}}
                    />
                ))}

                {card.dueDate && (
                    <span title={card.dueDate}>
            <CalendarIcon className="w-4 h-4 text-gray-500"/>

          </span>
                )}
            </div>
        </div>
    );
};

export default CardPreview;