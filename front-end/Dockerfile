FROM node:16.20-alpine as build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# CMD [ "npm", "start" ]

FROM nginx:1.25.3-alpine

# Copy the built app from the previous stage to the Nginx public directory
COPY --from=build /app/build /usr/share/nginx/html

# Adjust permissions
RUN chown -R nginx:nginx /usr/share/nginx/html

EXPOSE 80

CMD [ "nginx", "-g", "daemon off;" ]