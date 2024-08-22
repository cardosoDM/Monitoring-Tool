db = db.getSiblingDB('monitoringtool');

db.createCollection(
    "monitoringResults",
    {
        timeseries: {
            timeField: "timestamp",
            metaField: "metadata"
        }
    })