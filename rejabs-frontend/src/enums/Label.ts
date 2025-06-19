export const LABEL_OPTIONS = [
    {name:"URGENT" , color: "#FF4D4D"} ,
    { name: "DESIGN", color: "plum" },
    { name: "BACKEND", color: "darkolivegreen" },
    { name: "FRONTEND", color: "salmon" },
    { name: "BLOCKED", color: "tomato" },
    { name: "REVIEW", color: "chocolate" },
    { name: "QA", color: "olivedrab" },
    { name: "TODO", color: "#4DB8FF" },
    { name: "IN_PROGRESS", color: "darkorange" },
    { name: "DONE", color: "limegreen" },
    { name: "HIGH_PRIORITY", color: "orangered" },
    { name: "MEDIUM_PRIORITY", color: "coral" },
    { name: "LOW_PRIORITY", color: "#23b07f" },
    { name: "BUG", color: "red" },
    { name: "FEATURE", color: "#d243d2" },
    { name: "PLAN", color: "#328cd9" }
] as const;

export type Label = typeof LABEL_OPTIONS[number];