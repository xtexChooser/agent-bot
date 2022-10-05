FROM node:current-alpine
WORKDIR /app
COPY build/js/ /app/
CMD ["node", "packages/agent-bot/kotlin/agent-bot.js"]
