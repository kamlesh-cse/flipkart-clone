/**
 * Static FAQ Chatbot for FlipkartClone
 * Simple predefined Q&A chatbot - no AI needed.
 * Appears as a floating button on all customer pages.
 */

// Predefined FAQ questions and answers
const faqData = [
    {
        question: "How do I place an order?",
        answer: "Browse products on the home page, click 'Add to Cart', then go to your Cart and click 'Proceed to Checkout'. Fill in your shipping address and place the order!"
    },
    {
        question: "How can I track my order?",
        answer: "After placing an order, go to 'My Orders' from the navigation menu. You can see the current status of all your orders there."
    },
    {
        question: "What payment methods are accepted?",
        answer: "We accept Cash on Delivery (COD), UPI Payment, and Debit/Credit Cards."
    },
    {
        question: "How do I return a product?",
        answer: "Currently, our return policy allows returns within 7 days of delivery. Please contact us via the Contact Us page for return requests."
    },
    {
        question: "How do I reset my password?",
        answer: "Click on 'Forgot Password' on the login page. Enter your registered email, and we'll send you a password reset link."
    },
    {
        question: "Is my personal data safe?",
        answer: "Yes! We use encrypted passwords and secure session management to protect your data. We never share your information with third parties."
    },
    {
        question: "How do I contact customer support?",
        answer: "You can reach us via the Contact Us page, email us at support@flipkartclone.com, or call +91 98765 43210."
    },
    {
        question: "Can I cancel my order?",
        answer: "You can request order cancellation if the order status is still 'PLACED'. Please contact customer support for cancellation."
    }
];

// Create chatbot HTML and inject into page
function initChatbot() {
    const chatbotHTML = `
        <div id="chatbot-container">
            <div id="chatbot-box">
                <div id="chatbot-header">
                    <span><i class="bi bi-robot"></i> FAQ Assistant</span>
                    <button class="close-btn" onclick="toggleChatbot()">&times;</button>
                </div>
                <div id="chatbot-messages">
                    <div class="chat-msg bot">
                        Hi! I'm the FAQ Assistant. Click on a question below to get help!
                    </div>
                </div>
                <div id="chatbot-faq">
                    ${faqData.map((faq, index) =>
                        `<button class="faq-btn" onclick="askQuestion(${index})">${faq.question}</button>`
                    ).join('')}
                </div>
            </div>
            <button id="chatbot-toggle" onclick="toggleChatbot()" title="FAQ Chatbot">
                <i class="bi bi-chat-dots-fill"></i>
            </button>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', chatbotHTML);
}

// Toggle chatbot visibility
function toggleChatbot() {
    const chatbox = document.getElementById('chatbot-box');
    if (chatbox.style.display === 'block') {
        chatbox.style.display = 'none';
    } else {
        chatbox.style.display = 'block';
    }
}

// Handle FAQ question click
function askQuestion(index) {
    const faq = faqData[index];
    const messagesDiv = document.getElementById('chatbot-messages');

    // Add user message (the question)
    const userMsg = document.createElement('div');
    userMsg.className = 'chat-msg user';
    userMsg.textContent = faq.question;
    messagesDiv.appendChild(userMsg);

    // Add bot response (the answer) with a small delay for effect
    setTimeout(() => {
        const botMsg = document.createElement('div');
        botMsg.className = 'chat-msg bot';
        botMsg.textContent = faq.answer;
        messagesDiv.appendChild(botMsg);

        // Auto-scroll to bottom
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }, 500);

    // Scroll to show user message immediately
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

// Initialize chatbot when page loads
document.addEventListener('DOMContentLoaded', initChatbot);
