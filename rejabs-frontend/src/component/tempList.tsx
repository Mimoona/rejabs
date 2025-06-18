import {useState} from "react";
import CardList from "./CardList.tsx";

const tempList = () => {
    // for now Card code
    const [isCardDialogOpen, setIsCardDialogOpen] = useState(false);

    return (
        <div>
            {/*temporary list component*/}
            <CardList listId={"list123"}/>
            <div className="w-80 flex-shrink-0 bg-white rounded-2xl shadow p-4"
            >

                <button className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700">
                    Add Card
                </button>
            </div>


        </div>
    )
}
export default tempList;