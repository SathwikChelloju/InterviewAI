const question = document.getElementById("question");
const speakBtn = document.getElementById("speakBtn");

const interviewQuestions = [
    "What is Java?",
    "What is JVM?",
    "What is OOP?"
];

let currentQuestion = 0;

function speak(text){

    const speech = new SpeechSynthesisUtterance(text);

    speech.lang = "en-US";
    speech.rate = 0.95;
    speech.pitch = 1;

    window.speechSynthesis.speak(speech);
}

speakBtn.addEventListener("click",()=>{

    if(currentQuestion < interviewQuestions.length){

        question.innerHTML = interviewQuestions[currentQuestion];

        speak(interviewQuestions[currentQuestion]);

        currentQuestion++;

    }else{

        question.innerHTML="Interview Completed.";

        speak("Congratulations. Your interview is completed.");

    }

});