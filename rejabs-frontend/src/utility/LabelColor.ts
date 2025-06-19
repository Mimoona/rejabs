export const getColorFromLabel = (label: string) => {
    const labelColors: Record<string, string> = {
        HIGH_PRIORITY: "red",
        MEDIUM_PRIORITY: "coral",
        LOW_PRIORITY: "yellow",
        DESIGN: "purple",
        BACKEND: "green",
        FRONTEND: "orange",
        QA: "olivedrab",
        BUG: "darkorange",
        FEATURE: "#d243d2",
        PLAN: "#328cd9",
        TODO: "#23b07f",
        IN_PROGRESS: "chocolate",
        DONE: "limegreen",
        BLOCKER: "tomato",
        REVIEW: "green",



    };
    return labelColors[label] || "gray";
};