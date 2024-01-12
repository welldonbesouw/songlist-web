FROM node:20.9.0-alpine3.18 AS build
#FROM node:20.9.0-alpine3.18

WORKDIR /app

# Set Node.js heap size to 4 GB
ENV NODE_OPTIONS="--max-old-space-size=4096"

COPY package*.json ./

RUN npm install

COPY . ./

RUN npm run build

CMD [ "npm", "start" ]

FROM nginx:1.25.3-alpine

#RUN rm -rf /usr/share/nginx/html
# Copy the built app from the previous stage to the Nginx public directory
COPY --from=build /app/build /usr/share/nginx/html

# Adjust permissions
RUN chown -R nginx:nginx /usr/share/nginx/html

# EXPOSE 80

ENTRYPOINT [ "nginx", "-g", "daemon off;" ]
