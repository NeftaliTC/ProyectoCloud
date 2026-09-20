window.subirImagenesCloudinary = function (cloudName, uploadPreset, maxFiles) {
    return new Promise((resolve, reject) => {
        const urls = [];

        const widget = cloudinary.createUploadWidget(
            {
                cloudName: cloudName,
                uploadPreset: uploadPreset,
                multiple: true,
                maxFiles: maxFiles
            },
            (error, result) => {
                if (error) {
                    reject(error.message);
                    return;
                }
                if (result.event === "success") {
                    urls.push(result.info.secure_url);
                }
                if (result.event === "queues-end") {
                    widget.close();
                    resolve(urls);
                }
                if (result.event === "close") {
                    resolve(urls);
                }
            }
        );
        widget.open();
    });
};