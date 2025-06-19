import {CalendarIcon, PencilIcon} from "@heroicons/react/24/outline";
import type {Card} from "../types/Card.ts";
import {getColorFromLabel} from "../utility/LabelColor.ts";

interface Props {
    card: Card;
    onClick: () => void;
}

const CardPreview = ({ card, onClick }: Props) => {

    return (
        <div
            onClick={onClick}
            className="bg-gray-200 hover:bg-gray-300 rounded p-2 cursor-pointer shadow-sm transition"
        >
            <div className="flex items-center justify-between">
            <h3 className="text-sm font-medium text-gray-800 truncate">{card.cardTitle}</h3>
            <PencilIcon className="h-4 w-4" onClick={onClick}></PencilIcon>
            </div>
            <div>
                <p className="text-sm text-gray-600 truncate my-4">{card.description}</p>
            </div>

            <div className="flex items-center gap-1 mt-2">
                {card.labels.map((label) => (
                    <span
                        key={label}
                        className="w-3 h-3 rounded-full"
                        style={{ backgroundColor: getColorFromLabel(label) }}
                    />
                ))}

                {card.dueDate && (
                    <span title={card.dueDate}>
            <CalendarIcon className="w-4 h-4 text-gray-500" />

          </span>
                )}
            </div>
        </div>
    );
};

export default CardPreview;