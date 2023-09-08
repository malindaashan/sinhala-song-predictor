import axios from "axios";
import {BASE_URL} from "../common/Settings";

class DataSetCreatorService {
    saveAndExtractFeatures = async (algorithm, file) => {
        console.log(algorithm);
        const formData = new FormData();
        formData.append("file", file);
        formData.append("algorithm", JSON.stringify(algorithm));

        const {data: response} = await axios.post(BASE_URL + '/dataset-creator/save-extract-features', formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            });
        return response;
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new DataSetCreatorService();