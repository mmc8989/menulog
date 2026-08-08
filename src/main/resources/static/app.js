const API_BASE_URL = "https://menulog-app.onrender.com/api";

document.addEventListener("DOMContentLoaded", () => {
    if (document.body.classList.contains("auth-page")) {
        initLoginPage();
    } else {
        initCalendarPage();
    }
});

function initLoginPage() {
    const loginForm = document.getElementById("login-form");
    loginForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const username = document.getElementById("username").value;
        localStorage.setItem("loggedInUser", username);
        window.location.href = "index.html";
    });
}

let currentDate = new Date();
let selectedDateKey = "";
let diaries = JSON.parse(localStorage.getItem("menulogDiaries")) || {};

const weekdays = ["日", "月", "火", "水", "木", "金", "土"];

function initCalendarPage() {
    const user = localStorage.getItem("loggedInUser");
    if (!user) {
        window.location.href = "login.html";
        return;
    }
    document.getElementById("display-user").innerText = user;

    document.getElementById("logout-btn").addEventListener("click", () => {
        localStorage.removeItem("loggedInUser");
        window.location.href = "login.html";
    });

    document.getElementById("prev-btn").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar();
    });

    document.getElementById("next-btn").addEventListener("click", () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar();
    });

    document.getElementById("save-btn").addEventListener("click", saveDiary);
    document.getElementById("delete-btn").addEventListener("click", deleteDiary);
    document.getElementById("close-btn").addEventListener("click", () => {
        document.getElementById("entry-form").classList.add("hidden");
    });

    renderCalendar();
}

function renderCalendar() {
    const calendarListElem = document.getElementById("calendar-list");
    calendarListElem.innerHTML = "";

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    document.getElementById("year-text").innerText = year;
    document.getElementById("month-text").innerText = month + 1;

    const lastDate = new Date(year, month + 1, 0).getDate();

    // 1日〜最終日まで縦並びでループ生成
    for (let day = 1; day <= lastDate; day++) {
        const targetDate = new Date(year, month, day);
        const dayOfWeek = targetDate.getDay(); // 0:日, 1:月, ... 6:土

        const dateKey = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
        const diaryText = diaries[dateKey] || "";

        const row = document.createElement("div");
        row.classList.add("day-row");

        // 土日のクラス付与
        if (dayOfWeek === 0) row.classList.add("sun");
        if (dayOfWeek === 6) row.classList.add("sat");

        row.innerHTML = `
      <div class="day-info">
        <span class="weekday">${weekdays[dayOfWeek]}</span>
        <span class="day-num">${day}</span>
      </div>
      <div class="diary-content ${diaryText ? "" : "empty"}">
        ${diaryText || "一言を入力..."}
      </div>
    `;

        row.addEventListener("click", () => {
            selectedDateKey = dateKey;
            document.getElementById("selected-date").innerText =
                `${month + 1}月${day}日 (${weekdays[dayOfWeek]}) の日記`;
            document.getElementById("diary-input").value = diaries[dateKey] || "";
            document.getElementById("entry-form").classList.remove("hidden");
        });

        calendarListElem.appendChild(row);
    }
}

function saveDiary() {
    if (!selectedDateKey) return;
    const text = document.getElementById("diary-input").value.trim();
    if (text) {
        diaries[selectedDateKey] = text;
    } else {
        delete diaries[selectedDateKey];
    }
    localStorage.setItem("menulogDiaries", JSON.stringify(diaries));
    document.getElementById("entry-form").classList.add("hidden");
    renderCalendar();
}

function deleteDiary() {
    if (!selectedDateKey) return;
    delete diaries[selectedDateKey];
    localStorage.setItem("menulogDiaries", JSON.stringify(diaries));
    document.getElementById("entry-form").classList.add("hidden");
    renderCalendar();
}
