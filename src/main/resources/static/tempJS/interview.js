const question = document.getElementById("question");
const progress = document.getElementById("progress");
const answer = document.getElementById("answer");
const speakBtn = document.getElementById("speakBtn");

const interviewQuestions = [
    "What is Java?",
    "What is JVM?",
    "What is OOP?"
];

let currentQuestion = 0;

const recognition = new webkitSpeechRecognition();

recognition.lang = "en-US";
recognition.continuous = false;
recognition.interimResults = false;

function speak(text){

    const speech = new SpeechSynthesisUtterance(text);

    speech.rate = 0.95;

    window.speechSynthesis.speak(speech);

}

function askQuestion(){

    if(currentQuestion >= interviewQuestions.length){

        question.innerHTML="Interview Completed";
        progress.innerHTML="Completed";
        answer.value="";
        speak("Congratulations. You have completed the interview.");
        speakBtn.disabled=true;
        return;

    }

    progress.innerHTML="Question "+(currentQuestion+1)+" of "+interviewQuestions.length;

    question.innerHTML=interviewQuestions[currentQuestion];

    answer.value="";

    speak(interviewQuestions[currentQuestion]);

    setTimeout(()=>{
        recognition.start();
    },3000);

}

recognition.onresult=function(event){

    answer.value=event.results[0][0].transcript;

    currentQuestion++;

    setTimeout(()=>{
        askQuestion();
    },2000);

};

speakBtn.onclick=function(){

    speakBtn.innerHTML="Interview Running";

    askQuestion();

};